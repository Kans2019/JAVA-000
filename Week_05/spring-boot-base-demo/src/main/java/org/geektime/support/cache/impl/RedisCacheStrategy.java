package org.geektime.support.cache.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.geektime.support.cache.CacheStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Redis 实现缓存
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
public class RedisCacheStrategy implements CacheStrategy {
    private static final Logger logger = Logger.getLogger(RedisCacheStrategy.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void put(String key, Object value, long timeout) {
        String val = JSON.toJSONString(value);
        redisTemplate.opsForValue().set(key, val, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws NoSuchElementException {
        if (!redisTemplate.hasKey(key)) {
            throw new NoSuchElementException(key + " not exists");
        }
        String val = redisTemplate.opsForValue().get(key);
        if (logger.isDebugEnabled()) {
            logger.debug("从缓存中取到 " + key + " ===> " + val);
        }
        return (T) JSONObject.parseObject(val, clazz);
    }
}
