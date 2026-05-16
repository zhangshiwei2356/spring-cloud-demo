package com.cloud.common.util;

import java.util.Optional;

/**
 * Redis 接入占位：演示不设中间件，方法均为空操作。
 * <p>接入生产时：引入 {@code spring-boot-starter-data-redis}（或 Redisson）、注入 {@code StringRedisTemplate}，
 * 将本类方法委托给 {@code opsForValue()} 等与下方注释位置对应即可。</p>
 */
public final class RedisUtils {

    public void set(String key, String value) {
        // 占位 → stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long ttlSeconds) {
        // 占位 → stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    public Optional<String> get(String key) {
        // 占位 → Optional.ofNullable(stringRedisTemplate.opsForValue().get(key));
        return Optional.empty();
    }

    public Boolean delete(String key) {
        // 占位 → Boolean.TRUE.equals(stringRedisTemplate.delete(key));
        return Boolean.FALSE;
    }

    public Boolean expire(String key, long ttlSeconds) {
        // 占位 → Boolean.TRUE.equals(stringRedisTemplate.expire(...));
        return Boolean.FALSE;
    }
}
