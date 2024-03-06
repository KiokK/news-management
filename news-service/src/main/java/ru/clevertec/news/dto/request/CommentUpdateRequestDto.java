package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequestDto(

        @NotBlank
        String text
) {
}
