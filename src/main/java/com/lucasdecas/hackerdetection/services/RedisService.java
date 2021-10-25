package com.lucasdecas.hackerdetection.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService implements CacheService{

    private final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public void setWithExpirationTime(String key, Integer value, Integer minutes) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
    }

    public void incrementAndExpire(String key, Integer value, Integer minutes){
        redisTemplate.opsForValue().increment(key,value);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
    }

    public Integer get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
