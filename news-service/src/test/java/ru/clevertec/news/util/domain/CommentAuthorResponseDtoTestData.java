package ru.clevertec.news.util.domain;

import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.response.CommentAuthorResponseDto;
import ru.clevertec.news.dto.response.UserManagementDto;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.model.Comment;

public class CommentAuthorResponseDtoTestData {

    private static final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public static CommentAuthorResponseDto getCommentAuthorResponseDto(Comment comment, UserManagementDto userDto) {
        return commentMapper.toCommentAuthorResponseDto(comment, userDto);
    }
}
