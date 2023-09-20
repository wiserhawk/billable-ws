package com.indhawk.billable.billablews.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class CommonUtils {

    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static LocalDateTime strToDate(String date) {
        // String format must be dd-mm-yyyy
        String[] tokens = date.split("-");
        return LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]),Integer.parseInt(tokens[0]), 0, 0);
    }

    public static boolean patternMatches(String value, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(value)
                .matches();
    }

    public static boolean isEmptyString(String str) {
        return str == null || "".equals(str);
    }

    public static String[] splitCommaStringToArray(String str) {
        if (isEmptyString(str)) {
            throw new NullPointerException("String is null or empty");
        }
        return str.split(",");
    }
}
