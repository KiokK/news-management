package ru.clevertec.news.util.domain;

import org.mapstruct.factory.Mappers;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.News;

import java.util.List;

import static ru.clevertec.news.util.domain.model.NewsTestData.getNewsBuilder;

public class NewsResponseDtoTestData {

    private static final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    public static NewsResponseDto getNewsResponseDto(News news) {
        return newsMapper.toNewsResponseDto(news);
    }

    public static NewsResponseDto getNewsResponseDto() {
        return newsMapper.toNewsResponseDto(getNewsBuilder().build());
    }

    public static List<NewsResponseDto> getListNewsResponseDto(List<News> listNews) {
        return newsMapper.listNewsToListNewsResponseDto(listNews);
    }
}
