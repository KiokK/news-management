package ru.clevertec.authservice.dto.response;

import java.util.List;

public record UserPageResponseDto(
        List<UserResponseDto> users,
        int pageSize,
        int pageNumber
) {
}
