package ru.clevertec.news.util.domain;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;

import java.util.List;

import static ru.clevertec.news.util.domain.NewsResponseDtoTestData.getListNewsResponseDto;
import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsList;

public class NewsPageResponseDtoTestData {

    public static NewsPageResponseDto getNewsPageResponseDto(Pageable pageable) {
        List<NewsResponseDto> news = getListNewsResponseDto(getNewsList());

        return new NewsPageResponseDto(news, pageable.getPageSize(), pageable.getPageNumber());
    }
}
