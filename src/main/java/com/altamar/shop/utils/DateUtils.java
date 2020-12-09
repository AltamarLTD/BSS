package com.altamar.shop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    /**
     * @param date class
     * @return current Date
     */
    public static String generateCurrentDate(Date date) {
        final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        return sdf.format(date);
    }

}
