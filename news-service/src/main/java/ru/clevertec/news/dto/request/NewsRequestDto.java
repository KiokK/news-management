package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewsRequestDto(

        @NotBlank
        String text,

        @NotBlank
        String title
) {
}
