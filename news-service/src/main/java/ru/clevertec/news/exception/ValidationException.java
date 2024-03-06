package ru.clevertec.news.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String field, String value) {
        super(String.format("Not valid value %s='%s'", field, value));
    }

    public ValidationException(String field, Class clazz) {
        super(String.format("Not valid field %s in '%s'", field, clazz));
    }
}
