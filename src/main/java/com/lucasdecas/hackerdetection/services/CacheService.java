package com.lucasdecas.hackerdetection.services;

import org.springframework.stereotype.Service;

@Service
public interface CacheService {

    void setWithExpirationTime(String key, Integer value, Integer ttl);

    void incrementAndExpire(String key, Integer value, Integer ttl);

    Integer get(String key);

    Boolean exists(String key);
}
