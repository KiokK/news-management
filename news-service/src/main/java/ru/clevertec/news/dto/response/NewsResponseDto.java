package ru.clevertec.news.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.clevertec.news.dto.PatternsConstants.DATA_TIME_FORMAT;

public record NewsResponseDto(

        Long id,
        String text,
        String title,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime createdAt,

        @JsonFormat(pattern = DATA_TIME_FORMAT)
        LocalDateTime modifiedAt) {
}