package ru.clevertec.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import ru.clevertec.authservice.model.Role;

import static ru.clevertec.authservice.dto.PatternsConstants.PASSWORD_REGXP;
import static ru.clevertec.authservice.dto.PatternsConstants.USERNAME_REGXP;

public record UserRequestDto(

        @Pattern(regexp = USERNAME_REGXP)
        String username,

        @Email
        String email,

        @Pattern(regexp = PASSWORD_REGXP)
        String password,

        Role role
) {
}
