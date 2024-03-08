package ru.clevertec.news.util.domain;

import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.request.CommentRequestDto;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.util.domain.model.NewsTestData;

import static ru.clevertec.news.util.domain.model.CommentTestData.TEXT;
import static ru.clevertec.news.util.domain.model.CommentTestData.USERNAME;

public class CommentRequestDtoTestData {

    private static final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public static CommentRequestDto getCommentRequestDto() {
        return new CommentRequestDto(TEXT, USERNAME, NewsTestData.NEWS_ID);
    }

    public static CommentRequestDto getCommentRequestDto(Comment comment) {
        return commentMapper.toCommentRequestDto(comment);
    }

}
