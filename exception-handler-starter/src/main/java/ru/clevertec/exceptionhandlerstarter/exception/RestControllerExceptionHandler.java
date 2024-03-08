package ru.clevertec.exceptionhandlerstarter.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.REQUEST_NOT_VALID;

@Configuration
@RestControllerAdvice
@ConditionalOnProperty(value = "exception-handler.status", havingValue = "enabled")
public class RestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public @ResponseBody ErrorResponseDto handleEntityNotFoundException(EntityNotFoundException e) {

        return new ErrorResponseDto(e.getMessage(), ENTITY_NOT_FOUND);
    }

    /**
     * Обрабатывает ошибки валидации в контроллерах
     * @param e
     * @return объект класса {@link ErrorResponseDto} c {@link ErrorResponseDto#errorMessage}, который содержит все сообщения об ошибках
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ConditionalOnProperty(value = "exception-handler.validation", havingValue = "enabled")
    public @ResponseBody ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ErrorResponseDto(errors.toString(), REQUEST_NOT_VALID);
    }
}
