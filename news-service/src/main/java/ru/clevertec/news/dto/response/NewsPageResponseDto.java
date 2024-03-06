package ru.clevertec.news.dto.response;

import java.util.List;

public record NewsPageResponseDto(

        List<NewsResponseDto> news,
        int pageSize,
        int pageNumber
) {
}
