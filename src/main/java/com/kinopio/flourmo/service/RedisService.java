package com.kinopio.flourmo.service;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedissonClient redissonClient;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void setValue(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    public String getValue(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }
}