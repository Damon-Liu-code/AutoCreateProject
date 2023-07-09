package com.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 设置缓存
     *
     * @param key    缓存key
     * @param value  缓存value
     * @param expire 过期时间（单位：秒），传入-1表示永不过期
     */
    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return 缓存value
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存key
     * @return true：存在，false：不存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
