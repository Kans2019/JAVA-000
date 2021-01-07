package org.geektime.redis.app;

import org.geektime.redis.support.RedisCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模拟分布式减库存的线程
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
public class RedisCountThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(RedisCountThread.class);

    private final RedisCount redisCount;

    private final long count;

    public RedisCountThread(RedisCount redisCount, long count) {
        this.redisCount = redisCount;
        this.count = count;
    }

    @Override
    public void run() {
        logger.info("减库存[{}]{}", count, redisCount.desc(count) ? "成功" : "失败");
    }
}
