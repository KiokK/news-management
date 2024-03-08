package ru.clevertec.news.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

public interface CommentService {

    /**
     * Метод создания объекта {@link Comment}
     *
     * @param dto объект {@link CommentRequestDto}, содержащий данные для создания комментария
     * @return объект {@link CommentResponseDto} с информацией {@link Comment}
     * @throws EntityNotFoundException если объект не найден по username
     */
    CommentResponseDto create(CommentRequestDto dto) throws EntityNotFoundException;

    /**
     * Метод поиска объекта {@link Comment} по id
     *
     * @param id идентификатор для поиска объекта {@link Comment}
     * @return объект {@link CommentResponseDto} с информацией {@link Comment}
     * @throws EntityNotFoundException если объект не найден
     */
    CommentResponseDto findById(Long id) throws EntityNotFoundException;

    /**
     * Метод поиска объекта {@link Comment} с информацией об авторе по id. Информация об авторе
     * получается через метод {@link ManagementUserClient#getUserByUsername}
     *
     * @param id идентификатор для поиска объекта {@link Comment}
     * @return объект {@link CommentAuthorResponseDto} с информацией {@link Comment}
     * @throws EntityNotFoundException если объект не найден
     */
    CommentAuthorResponseDto findCommentWithAuthorById(Long id) throws EntityNotFoundException;

    /**
     * Метод поиска объекта {@link Comment} с информацией о новости {@link News}, которой принадлежит комментарий
     *
     * @param newsId идентификатор для поиска объекта {@link News}
     * @param commentId идентификатор для поиска объекта {@link Comment}
     * @return объект {@link NewsWithCommentResponseDto} с информацией о новости и комментарии
     * @throws EntityNotFoundException если объект не найден
     */
    NewsWithCommentResponseDto findByNewsIdAndCommentId(Long newsId, Long commentId) throws EntityNotFoundException;

    /**
     * Метод постраничного поиска объектов {@link Comment}
     *
     * @param pageable объект для разбивки информации на страницы
     * @return объект {@link CommentsPageResponseDto} с информацией о странице и списком комментариев
     */
    CommentsPageResponseDto findAll(Pageable pageable);

    /**
     * @param newsId идентификатор для поиска объекта {@link News}
     * @param pageable объект для разбивки информации на страницы
     * @return объект {@link NewsWithPageCommentsResponseDto} с информацией о новости, странице и списком комментариев
     */
    NewsWithPageCommentsResponseDto findAllByNewsId(Long newsId, Pageable pageable);

    /**
     * @param pageable объект для разбивки информации на страницы
     * @param specification
     * @return объект {@link CommentsPageResponseDto} с информацией о странице и списком комментариев
     */
    CommentsPageResponseDto findAllByFilter(Pageable pageable, Specification<Comment> specification);

    /**
     * @param filter объект фильтр для поиска комментариев
     * @param pageable объект для разбивки информации на страницы
     * @return объект {@link CommentsPageResponseDto} с информацией о странице и списком комментариев
     * @throws ValidationException когда Filter содержит некорректные данные для поиска комментария
     */
    CommentsPageResponseDto findAllByFilter(Filter filter, Pageable pageable) throws ValidationException;

    /**
     * @param id идентификатор для поиска объекта {@link Comment}
     * @param dto объект с информацией для обновления комментария
     * @return объект {@link CommentResponseDto} с информацией {@link Comment}
     * @throws EntityNotFoundException если объект не найден
     */
    CommentResponseDto update(Long id, CommentUpdateRequestDto dto) throws EntityNotFoundException;

    /**
     * @param id идентификатор для удаления объекта {@link Comment}
     */
    void deleteById(Long id);
}
