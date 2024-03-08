package ru.clevertec.authservice.util;

import ru.clevertec.authservice.model.Role;
import ru.clevertec.authservice.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserTestData {

    public static final long USER_ID = 1L;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "QWer12qwqw_";
    public static final String EMAIL = "email@mail.com";
    public static final Role ROLE_ADMIN = Role.ADMIN;
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2024,1,2,2,2,2);
    public static final LocalDateTime MODIFIED_AT = LocalDateTime.of(2024,1,3,2,2,2);

    public static User.UserBuilder getUserBuilder() {

        return User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .role(ROLE_ADMIN)
                .createdAt(CREATED_AT)
                .modifiedAt(MODIFIED_AT);
    }

    public static List<User> getUserList() {

        return List.of(getUserBuilder().build());
    }
}
