package org.geektime.redis.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Component
public class RedisLock {
    private final static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /**
     * 没有拿到锁时是否阻塞线程
     */
    @Value("${geektime.lock.block:false}")
    private boolean block;

    /**
     * 锁的超时时间, 单位ms
     */
    @Value("${geektime.lock.timeout:10000}")
    private long timeout;

    @Value("${geektime.lock.sleep:1000}")
    private long sleep;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 释放锁
     * @param key
     */
    public void release(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 拿到锁
     * @param key
     * @return
     */
    public boolean lock(String key) {
        long start = System.currentTimeMillis();
        long value = start + timeout;
        while (Objects.equals(redisTemplate.opsForValue().setIfAbsent(key, value), Boolean.FALSE)) {
            Long lockValue = Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse(start);
            if (lockValue < start) {
                Long newValue = redisTemplate.opsForValue().getAndSet(key, value);
                if (Objects.equals(newValue, lockValue)) {
                    logger.info("start = {},lockValue = {}, newValue = {}", start, newValue, lockValue);
                    redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
                    return true;
                }
            }
            if (block) {
                try {
                    long tmp = sleep >>> 1;
                    Thread.sleep(tmp + ThreadLocalRandom.current().nextLong(sleep));
                } catch (InterruptedException e) {
                    logger.error("线程阻塞失败", e);
                }
            } else {
                return false;
            }
            // 尝试重新获取锁
            start = System.currentTimeMillis();
            value = start + timeout;
        }
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
        return true;
    }
}
