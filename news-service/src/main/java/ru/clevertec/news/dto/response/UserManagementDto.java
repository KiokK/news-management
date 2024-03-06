package ru.clevertec.news.dto.response;

public record UserManagementDto (
        Long id,
        String username,
        String email
) {
}
