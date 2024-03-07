package ru.clevertec.authservice.util;

import ru.clevertec.authservice.dto.response.UserResponseDto;

import java.util.List;

import static ru.clevertec.authservice.util.UserTestData.EMAIL;
import static ru.clevertec.authservice.util.UserTestData.USERNAME;
import static ru.clevertec.authservice.util.UserTestData.USER_ID;

public class UserResponseDtoTestData {

    public static UserResponseDto getUserResponseDto() {

        return new UserResponseDto(USER_ID, USERNAME, EMAIL);
    }

    public static UserResponseDto getUserResponseDtoWithBlankName() {

        return new UserResponseDto(USER_ID, "", EMAIL);
    }

    public static List<UserResponseDto> getListUserResponseDto() {

        return List.of(new UserResponseDto(USER_ID, USERNAME, EMAIL));
    }
}
