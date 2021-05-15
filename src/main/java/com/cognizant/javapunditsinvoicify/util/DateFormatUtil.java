package com.cognizant.javapunditsinvoicify.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    private static final String DATE_FORMAT  = "MMM d, yyyy HH:mm a - zzzz";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static String formatDate(ZonedDateTime zonedDateTime){
        return zonedDateTime.format(formatter);
    }

}
