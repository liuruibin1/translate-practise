package com.xxx.common.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValid(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }

}