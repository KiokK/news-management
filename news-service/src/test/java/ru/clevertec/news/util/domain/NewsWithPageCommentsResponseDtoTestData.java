package ru.clevertec.news.util.domain;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.model.News;

public class NewsWithPageCommentsResponseDtoTestData {

    public static NewsWithPageCommentsResponseDto getNewsWithPageCommentsResponseDto(News news, Pageable commentPageable) {

        return new NewsWithPageCommentsResponseDto(
                news.getId(),
                news.getText(),
                news.getTitle(),
                CommentResponseDtoTestData.getListCommentResponseDto(news.getComments()),
                commentPageable.getPageSize(),
                commentPageable.getPageNumber(),
                news.getCreatedAt(),
                news.getModifiedAt());
    }
}
