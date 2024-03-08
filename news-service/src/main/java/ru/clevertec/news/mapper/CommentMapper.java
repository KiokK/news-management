package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.dto.request.CommentUpdateRequestDto;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.CommentsPageResponseDto;
import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;
import ru.clevertec.news.dto.response.UserManagementDto;
import ru.clevertec.news.model.Comment;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "newsId", source = "news.id")
    CommentResponseDto toCommentResponseDto(Comment comment);

    List<CommentResponseDto> listCommentsToListCommentResponseDto(List<Comment> comments);

    @Mapping(target = "news.id", source = "newsId")
    Comment toComment(CommentRequestDto commentRequestDto);

    @Mapping(target = "newsId", source = "comment.news.id")
    CommentRequestDto toCommentRequestDto(Comment comment);

    @Mapping(target = "text", source = "newData.text")
    Comment mergeCommentRequestDto(Comment comment, CommentUpdateRequestDto newData);

    @Mapping(target = "comments", source = "comments")
    CommentsPageResponseDto toCommentsPageResponseDto(List<Comment> comments, Pageable pageable);

    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "news", source = "comment.news")
    NewsWithCommentResponseDto toNewsWithCommentResponseDto(Comment comment);

    @Mapping(target = "commentId", source = "comment.id")
    @Mapping(target = "userId", source = "userDto.id")
    @Mapping(target = "username", source = "userDto.username")
    @Mapping(target = "newsId", source = "comment.news.id")
    CommentAuthorResponseDto toCommentAuthorResponseDto(Comment comment, UserManagementDto userDto);

}
