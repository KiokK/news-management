package ru.clevertec.news.util.domain;

import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.News;

import static ru.clevertec.news.util.domain.model.NewsTestData.TITLE;
import static ru.clevertec.news.util.domain.model.NewsTestData.TEXT;

public class NewsRequestDtoTestData {

    private static final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    public static NewsRequestDto getNewsRequestDtoTestData() {
        return new NewsRequestDto(TEXT, TITLE);
    }

    public static NewsRequestDto getNewsRequestDtoTestData(News news) {
        return newsMapper.toNewsRequestDto(news);
    }
}
