package com.example.demo.redis;

import java.time.Duration;

public interface RedisITF {
    void setValue(String key, Object value);
    void setValue(String key, Object value, Duration ttl);
    <T> T getValue(String key, Class<T> clazz);
    void deleteValue(String key);
    boolean hasKey(String key);
}
