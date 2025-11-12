package com.example.demo.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService implements RedisITF {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; // Inject từ cấu hình JacksonConfig

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValue(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return objectMapper.convertValue(value, clazz);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null && exists;
    }
}
