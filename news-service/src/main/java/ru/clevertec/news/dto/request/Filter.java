package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record Filter(

        @Size(min = 1)
        String part,

        @NotNull
        List<String> fields
) {
}
