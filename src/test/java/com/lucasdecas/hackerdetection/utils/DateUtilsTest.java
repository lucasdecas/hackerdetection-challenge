package com.lucasdecas.hackerdetection.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {

    @Test
    public void shouldConvertEpochToRFCTest(){

        String expected = "Fri, 4 May 2012 11:04:31 +0000";
        String epochTime = "1336129471";

        String rfcTime = DateUtils.convertEpochToRFC(epochTime);
        assertEquals(expected, rfcTime);
    }

    @Test
    public void shouldGetDifferenceBetweenTimesInMinutesTest(){

        Long expected = 4L;
        String date1 = "Fri, 4 May 2012 11:00:00 +0000";
        String date2 = "Fri, 4 May 2012 11:04:00 +0000";

        Long difference = DateUtils.getDifferenceInMinutes(date1,date2);
        assertEquals(expected, difference);
    }

    @Test
    public void shouldGetDifferenceBetweenTimesInMinutesRoundingTest(){

        Long expected = 4L;
        String date1 = "Fri, 4 May 2012 11:00:00 +0000";
        String date2 = "Fri, 4 May 2012 11:04:59 +0000";

        Long difference = DateUtils.getDifferenceInMinutes(date1,date2);
        assertEquals(expected, difference);
    }
}
