package ru.clevertec.news.validation;

import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.exception.ValidationException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchFilterValidation {

    public static void validate(Filter filter, Class clazz) {
        List<String> fields = Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList();

        Optional<ValidationException> isException = filter.fields().stream()
                .filter(f -> !fields.contains(f))
                .map(field -> new ValidationException(field, clazz))
                .findAny();

        if (isException.isPresent()) {
            throw isException.get();
        }
    }
}
