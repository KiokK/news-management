package ru.clevertec.authservice.dto;

public class PatternsConstants {
    public static final String PASSWORD_REGXP = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&?_\"]).*$";
    public static final String USERNAME_REGXP = "^(?=[a-zA-Z0-9._]{4,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";
}
