package com.awesome.bank.utility;

import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {

    /**
     * Formats a date to string
     */
    public String toDateString(OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
