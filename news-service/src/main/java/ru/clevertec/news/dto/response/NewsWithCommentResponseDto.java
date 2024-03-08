package ru.clevertec.news.dto.response;

public record NewsWithCommentResponseDto(

        CommentResponseDto comment,
        NewsResponseDto news
) {
}
