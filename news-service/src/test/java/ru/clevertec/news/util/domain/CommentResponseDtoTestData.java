package ru.clevertec.news.util.domain;

import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.model.Comment;

import java.util.List;

import static ru.clevertec.news.util.domain.model.CommentTestData.COMMENT_ID;
import static ru.clevertec.news.util.domain.model.CommentTestData.CREATED_AT;
import static ru.clevertec.news.util.domain.model.CommentTestData.MODIFIED_AT;
import static ru.clevertec.news.util.domain.model.CommentTestData.TEXT;
import static ru.clevertec.news.util.domain.model.CommentTestData.USERNAME;
import static ru.clevertec.news.util.domain.model.NewsTestData.NEWS_ID;

public class CommentResponseDtoTestData {

    private static final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public static CommentResponseDto getCommentResponseDto() {
        return new CommentResponseDto(COMMENT_ID, TEXT, USERNAME, NEWS_ID, CREATED_AT, MODIFIED_AT);
    }

    public static CommentResponseDto getCommentResponseDto(String text) {
        return new CommentResponseDto(COMMENT_ID, text, USERNAME, NEWS_ID, CREATED_AT, MODIFIED_AT);
    }

    public static CommentResponseDto getCommentResponseDto(Comment comment) {
        return commentMapper.toCommentResponseDto(comment);
    }

    public static List<CommentResponseDto> getListCommentResponseDto(List<Comment> comments) {
        return commentMapper.listCommentsToListCommentResponseDto(comments);
    }
}
