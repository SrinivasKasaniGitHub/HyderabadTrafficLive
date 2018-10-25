package com.tspolice.htplive.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static SimpleDateFormat simpleDateFormat;

    public static String getCurrentTimeStamp() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }
}
