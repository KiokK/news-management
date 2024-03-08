package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cachestarter.cache.proxy.DeleteFromCache;
import ru.clevertec.cachestarter.cache.proxy.GetFromCache;
import ru.clevertec.cachestarter.cache.proxy.PostFromCache;
import ru.clevertec.cachestarter.cache.proxy.PutToCache;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.news.client.ManagementUserClient;
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.dto.response.UserManagementDto;
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.CommentService;

import java.util.List;
import java.util.Optional;

import static ru.clevertec.news.config.SecurityConfig.AUTHORIZATION_TYPE;
import static ru.clevertec.news.validation.SearchFilterValidation.validate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    private final ManagementUserClient userClient;

    @Override
    @PutToCache
    @Transactional
    public CommentResponseDto create(CommentRequestDto dto) throws EntityNotFoundException {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Optional.ofNullable(userClient.getUserByUsername(dto.username(), AUTHORIZATION_TYPE + authentication.getToken().getTokenValue()).getBody())
                .orElseThrow(() -> new EntityNotFoundException("username", dto.username()));

        return Optional.of(dto)
                .map(commentMapper::toComment)
                .map(commentRepository::save)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow();
    }

    @Override
    @GetFromCache
    public CommentResponseDto findById(Long id) throws EntityNotFoundException {

        return commentRepository.findById(id)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public CommentAuthorResponseDto findCommentWithAuthorById(Long id) throws EntityNotFoundException {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        UserManagementDto userManagementDto = Optional.ofNullable(
                userClient.getUserByUsername(comment.getUsername(), AUTHORIZATION_TYPE + authentication.getToken().getTokenValue()).getBody()
        ).orElseThrow(() -> new EntityNotFoundException("username", comment.getUsername()));

        return commentMapper.toCommentAuthorResponseDto(comment, userManagementDto);
    }

    @Override
    public NewsWithCommentResponseDto findByNewsIdAndCommentId(Long newsId, Long commentId) throws EntityNotFoundException {
        NewsWithCommentResponseDto foundComment = commentRepository.findById(commentId)
                .map(commentMapper::toNewsWithCommentResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(commentId));

        if (foundComment.news().id() != newsId) {
            throw new EntityNotFoundException(newsId);
        }

        return foundComment;
    }

    @Override
    public CommentsPageResponseDto findAll(Pageable pageable) {
        List<Comment> comments = commentRepository.findAll(pageable).toList();

        return commentMapper.toCommentsPageResponseDto(comments, pageable);
    }

    @Override
    public NewsWithPageCommentsResponseDto findAllByNewsId(Long newsId, Pageable pageable) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(newsId));
        List<Comment> newsesComments = commentRepository.findAllByNews_Id(newsId, pageable)
                .getContent();
        news.setComments(newsesComments);

        return newsMapper.toNewsWithCommentsResponseDto(news, pageable);
    }

    @Override
    public CommentsPageResponseDto findAllByFilter(Pageable pageable, Specification<Comment> specification) {
        List<Comment> comments = Optional.ofNullable(specification)
                .map(spec -> commentRepository.findAll(spec, pageable))
                .orElseGet(() -> commentRepository.findAll(pageable))
                .toList();

        return commentMapper.toCommentsPageResponseDto(comments, pageable);
    }

    @Override
    public CommentsPageResponseDto findAllByFilter(Filter filter, Pageable pageable) throws ValidationException {
        validate(filter, Comment.class);

        List<String> searchFields = filter.fields();

        Specification<Comment> specification = Optional.of(filter.part())
                .map(part -> "%" + part + "%")
                .map(part -> Specification.anyOf(
                        searchFields.stream()
                                .map(field -> (Specification<Comment>) (root, query, cb) -> cb.like(root.get(field), part))
                                .toList()
                ))
                .orElse(null);

        return findAllByFilter(pageable, specification);
    }

    @Override
    @Transactional
    @PostFromCache
    public CommentResponseDto update(Long id, CommentUpdateRequestDto dto) throws EntityNotFoundException {
        return commentRepository.findById(id)
                .map(comment -> commentMapper.mergeCommentRequestDto(comment, dto))
                .map(commentRepository::save)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @Transactional
    @DeleteFromCache
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
