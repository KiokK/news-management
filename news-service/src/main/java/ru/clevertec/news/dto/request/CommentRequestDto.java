package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(

        @NotBlank
        String text,

        @NotBlank
        String username,

        @Min(1)
        Long newsId
) {
}
