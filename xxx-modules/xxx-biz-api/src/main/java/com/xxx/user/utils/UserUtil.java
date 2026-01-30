package com.xxx.user.utils;

import java.util.regex.Pattern;

public class UserUtil {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{2,31}$");

    public static boolean verifyUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

}
