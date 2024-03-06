package ru.clevertec.news.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;

import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.REQUEST_NOT_VALID;

@Configuration
@RestControllerAdvice
public class NewsRestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, PropertyReferenceException.class})
    public @ResponseBody ErrorResponseDto handleValidationExceptions(Throwable e) {

        return new ErrorResponseDto(e.getMessage(), REQUEST_NOT_VALID);
    }
}
