package ru.clevertec.authservice.dto.response;

public record UserResponseDto(
        Long id,
        String username,
        String email
) {
}
