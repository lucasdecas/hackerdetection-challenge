package com.lucasdecas.hackerdetection.utils;

import com.lucasdecas.hackerdetection.exceptions.DateUtilsException;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static String RFC_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

    public static String convertEpochToRFC(String epochTime) {
        if (epochTime == null) {
            return null;
        }

        try {
            Instant instant = Instant.ofEpochSecond(Long.parseLong(epochTime));
            ZonedDateTime utcTime = instant.atZone(ZoneId.of("UTC"));


            return DateTimeFormatter.ofPattern(RFC_PATTERN).format(utcTime);
        } catch(Exception e){
            throw new DateUtilsException(e.getMessage(),e.getCause());
        }
    }

    public static Long getDifferenceInMinutes(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

            ZonedDateTime time1 = ZonedDateTime.parse(date1, formatter);
            ZonedDateTime time2 = ZonedDateTime.parse(date2, formatter);
            Duration duration = Duration.between(time1, time2);
            return duration.toMinutes();
        } catch(Exception e){
            throw new DateUtilsException(e.getMessage(),e.getCause());
        }
    }

}
