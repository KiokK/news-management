package ru.clevertec.authservice.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;

import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.REQUEST_CONFLICT;

@Configuration
@RestControllerAdvice
public class NewsRestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class, PropertyReferenceException.class})
    public @ResponseBody ErrorResponseDto handleValidationExceptions(Throwable e) {

        return new ErrorResponseDto(e.getMessage(), ENTITY_NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserEntityException.class)
    public @ResponseBody ErrorResponseDto handleUserEntityException(Throwable e) {

        return new ErrorResponseDto(e.getMessage(), REQUEST_CONFLICT);
    }
}
