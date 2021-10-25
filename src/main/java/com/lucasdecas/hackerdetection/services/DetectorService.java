package com.lucasdecas.hackerdetection.services;

import com.hotelbeds.supplierintegrations.hackertest.detector.HackerDetector;
import com.lucasdecas.hackerdetection.models.SigninStatus;
import com.lucasdecas.hackerdetection.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DetectorService implements HackerDetector {

    private final Logger LOGGER = LoggerFactory.getLogger(DetectorService.class);

    private CacheService cacheService;
    private final String IP_KEY = "IP";
    private final String DATE_KEY = "DATE";
    private final String STATUS_KEY = "STATUS";
    private final String USERNAME_KEY = "USERNAME";
    private final Integer detectionPeriodMinutes = 5;

    @Autowired
    public DetectorService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public String parseLine(String line) {
            String[] lineItems = line.split(",");

            if (lineItems.length == 4) {
                try {
                    Map<String,String> items = mapLineItems(lineItems);
                    String ip = items.get(IP_KEY);
                    if(SigninStatus.SIGNIN_FAILURE.name().equals(items.get(STATUS_KEY))){
                        if(!cacheService.exists(ip)){
                            cacheService.setWithExpirationTime(ip,1, detectionPeriodMinutes);
                            return null;
                        }
                        Integer tries = cacheService.get(ip);
                        if(tries >= 5){
                            return ip;
                        } else {
                            cacheService.incrementAndExpire(ip,1, detectionPeriodMinutes);
                            return null;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Invalid Log Line information for assessment: {}, {}", line ,e);
                }
            }
            return null;
    }

    private Map<String,String> mapLineItems(String[] lineItems){
        Map<String, String> map = new HashMap<>();
        map.put(IP_KEY,lineItems[0]);
        map.put(DATE_KEY, DateUtils.convertEpochToRFC(lineItems[1]));
        map.put(STATUS_KEY, SigninStatus.valueOf(lineItems[2]).toString());
        map.put(USERNAME_KEY, lineItems[3]);
        return map;
    }
}


