package ru.clevertec.news.dto.response;

public record ErrorResponseDto (

        String errorMessage,
        int errorCode
){
}
