package ru.clevertec.authservice.util;

import ru.clevertec.authservice.dto.request.UserRequestDto;

import static ru.clevertec.authservice.util.UserTestData.EMAIL;
import static ru.clevertec.authservice.util.UserTestData.PASSWORD;
import static ru.clevertec.authservice.util.UserTestData.ROLE_ADMIN;
import static ru.clevertec.authservice.util.UserTestData.USERNAME;

public class UserRequestDtoTestData {

    public static UserRequestDto getUserRequestDto() {

        return new UserRequestDto(USERNAME, EMAIL, PASSWORD, ROLE_ADMIN);
    }

}
