package com.kinopio.flourmo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.kinopio.flourmo.property.AppRedisProperty;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword.isEmpty() ? null : redisPassword)
                .setDatabase(redisDatabase);
        return Redisson.create(config);
    }



    @Autowired
    private AppRedisProperty appRedisProperty;

    @Bean(name = "appRedisConnectionFactory")
    public RedisConnectionFactory appRedisConnectionFactory() {
        ///FIXME 还没有设置pool
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(appRedisProperty.getHost(), appRedisProperty.getPort());
        if (appRedisProperty.getPassword() != null) {
            standaloneConfig.setPassword(RedisPassword.of(appRedisProperty.getPassword()));
        }
        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(standaloneConfig);
        redisConnectionFactory.setDatabase(appRedisProperty.getDatabase());
        return redisConnectionFactory;
    }

    @Bean(name = "appStringRedisTemplate")
    public StringRedisTemplate appStringRedisTemplate(@Qualifier("appRedisConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();


        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决 LocalDateTime 凡序列化

        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        om.registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class, localDateTimeDeserializer));
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        stringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.afterPropertiesSet();

        return stringRedisTemplate;
    }

}