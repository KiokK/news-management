package ru.clevertec.news.util.domain;

import ru.clevertec.news.dto.response.NewsWithCommentResponseDto;

import static ru.clevertec.news.util.domain.NewsResponseDtoTestData.getNewsResponseDto;

public class NewsWithCommentResponseDtoTestData {

    public static NewsWithCommentResponseDto getNewsWithCommentResponseDto() {
        return new NewsWithCommentResponseDto(CommentResponseDtoTestData.getCommentResponseDto(), getNewsResponseDto());
    }
}
