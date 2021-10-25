package com.lucasdecas.hackerdetection.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DetectorServiceTest {

    CacheService cacheService = Mockito.mock(CacheService.class);
    DetectorService service = new DetectorService(cacheService);

    @Test
    public void shouldNotReturnIpWhenLoginWasSuccessfulTest(){
        String logLine = "80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith";
        assertNull(service.parseLine(logLine));
    }

    @Test
    public void shouldNotReturnIpWhenLineIncompleteTest(){
        String logLineNoIp = "133612947,SIGNIN_SUCCESS,Will.Smith";
        String logLineNoTime = "80.238.9.179,SIGNIN_SUCCESS,Will.Smith";
        String logLineNoStatus = "80.238.9.179,133612947,Will.Smith";
        String logLineNoName = "80.238.9.179,133612947,SIGNIN_SUCCESS";

        assertNull(service.parseLine(logLineNoIp));
        assertNull(service.parseLine(logLineNoTime));
        assertNull(service.parseLine(logLineNoStatus));
        assertNull(service.parseLine(logLineNoName));
    }

    @Test
    public void shouldNotReturnIpWhenEntryDoesNotExistTest(){
        when(cacheService.exists(any())).thenReturn(false);
        Mockito.doNothing().when(cacheService).setWithExpirationTime(any(),any(),any());

        String logLine = "80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith";
        assertNull(service.parseLine(logLine));
    }

    @Test
    public void shouldReturnIpWhenTooManyTriesTest(){
        when(cacheService.exists(any())).thenReturn(true);
        when(cacheService.get(any())).thenReturn(5);

        String logLine = "80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith";
        assertEquals("80.238.9.179", service.parseLine(logLine));
    }

    @Test
    public void shouldNotReturnIpWhenFewerTriesThanFiveTest(){
        when(cacheService.exists(any())).thenReturn(true);
        when(cacheService.get(any())).thenReturn(4);

        String logLine = "80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith";
        assertNull(service.parseLine(logLine));
    }
}
