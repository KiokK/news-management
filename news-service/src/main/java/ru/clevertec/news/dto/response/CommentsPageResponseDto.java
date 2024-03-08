package ru.clevertec.news.dto.response;

import java.util.List;

public record CommentsPageResponseDto(

        List<CommentResponseDto> comments,
        int pageSize,
        int pageNumber
) {
}
