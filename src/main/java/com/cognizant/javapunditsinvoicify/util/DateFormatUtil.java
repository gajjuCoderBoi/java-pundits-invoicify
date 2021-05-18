package com.cognizant.javapunditsinvoicify.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoicifyConstants.DATE_FORMAT);

    public static String formatDate(ZonedDateTime zonedDateTime){
        return zonedDateTime.format(formatter);
    }

}
