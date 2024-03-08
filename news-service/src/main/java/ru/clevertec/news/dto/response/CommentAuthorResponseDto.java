package ru.clevertec.news.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.clevertec.news.dto.PatternsConstants.DATA_TIME_FORMAT;

public record CommentAuthorResponseDto(

        Long commentId,
        Long userId,
        String text,
        String username,
        String email,
        Long newsId,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime createdAt,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime modifiedAt) {
}
