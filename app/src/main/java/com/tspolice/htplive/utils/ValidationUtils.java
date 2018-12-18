package com.tspolice.htplive.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidMobileNo(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 10 || phone.length() > 10) {
                // if(phone.length() != 10) {
                check = false;
            } else if (phone.length()==10){
                if (((phone.charAt(0)=='6')) || ((phone.charAt(0)=='7'))|| ((phone.charAt(0)=='8'))
                        || ((phone.charAt(0)=='9'))) {
                    check = true;
                }else {
                    check = false;
                }
            }
        } else {
            check = false;
        }
        return check;
    }
}
