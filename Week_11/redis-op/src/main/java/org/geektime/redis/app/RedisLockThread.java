package org.geektime.redis.app;

import org.geektime.redis.support.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 进行分布式锁测试的线程类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
public class RedisLockThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(RedisLockThread.class);

    private final RedisLock redisLock;

    private final String key;

    public RedisLockThread(RedisLock redisLock, String key) {
        this.redisLock = redisLock;
        this.key = key;
    }

    @Override
    public void run() {
        if (redisLock.lock(key)) {
            logger.info("拿到分布式锁[{}]", key);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("线程阻塞失败", e);
            }
            redisLock.release(key);
        } else {
            logger.info("没有拿到分布式锁[{}]", key);
        }
    }
}
