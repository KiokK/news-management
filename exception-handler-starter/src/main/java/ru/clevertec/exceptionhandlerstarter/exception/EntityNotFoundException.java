package ru.clevertec.exceptionhandlerstarter.exception;

/**
 * Exception to be thrown when entity not found.
 */
public class EntityNotFoundException extends RuntimeException {

    public static final String MESSAGE_BY_FIELD_NOT_FOUND = "Entity with %s='%s' not found";
    public static final String MESSAGE_WITH_ID = "Entity with id='%s' not found";
    public static final String MESSAGE_DEFAULT = "Entity is not found";

    public EntityNotFoundException() {
        super(MESSAGE_DEFAULT);
    }

    public EntityNotFoundException(Long id) {
        super(String.format(MESSAGE_WITH_ID, id));
    }

    public EntityNotFoundException(String field, Object value) {
        super(String.format(MESSAGE_BY_FIELD_NOT_FOUND, field, value));
    }

}
