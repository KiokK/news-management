package ru.clevertec.authservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserEntityException extends RuntimeException {

    public UserEntityException(String message) {
        super(message);
    }
}
