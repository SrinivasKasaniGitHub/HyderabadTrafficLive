package com.tspolice.htplive.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidMobile(final String phone) {
        boolean check = false;
        if (!Pattern.matches(Constants.isValidMobile, phone)) {
            if (phone.length() == 10) {
                check = ((phone.charAt(0) == '6')) || ((phone.charAt(0) == '7'))
                        || ((phone.charAt(0) == '8')) || ((phone.charAt(0) == '9'));
            }
        }
        return check;
    }
}
