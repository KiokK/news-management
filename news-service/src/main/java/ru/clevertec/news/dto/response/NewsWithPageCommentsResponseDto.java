package ru.clevertec.news.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.clevertec.news.dto.PatternsConstants.DATA_TIME_FORMAT;

public record NewsWithPageCommentsResponseDto(

        Long id,
        String text,
        String title,
        List<CommentResponseDto> comments,
        int pageSize,
        int pageNumber,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime createdAt,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime modifiedAt) {
}
