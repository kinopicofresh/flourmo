package com.kinopio.flourmo.service.impl;

import com.kinopio.flourmo.service.RedisService;
import com.kinopio.flourmo.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {

    private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

    @Autowired
    @Qualifier("appStringRedisTemplate")
    StringRedisTemplate appRedis;

    @Override
    public String getUnitName(String unitCode) {
        logger.info("unitCode: {}", unitCode);
        appRedis.opsForValue().get()
        String s = appRedis.opsForValue().get(unitCode);
        String value = s;
        logger.info("unitCode value: {}", value);
        return value;
        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getUnitName'");
    }

}
