package ru.clevertec.news.util.domain;

import ru.clevertec.news.dto.response.UserManagementDto;

public class UserManagementDtoTestData {

    public static long DEFAULT_USER_ID = 1L;
    public static String DEFAULT_USER_EMAIL = "userEmail1@Gmail.com";

    public static UserManagementDto getUserManagementDto(String username) {

        return new UserManagementDto(DEFAULT_USER_ID, username, DEFAULT_USER_EMAIL);
    }
}
