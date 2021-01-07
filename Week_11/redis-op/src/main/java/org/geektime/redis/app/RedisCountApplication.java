package org.geektime.redis.app;

import org.geektime.redis.support.RedisCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Component
public class RedisCountApplication implements ApplicationRunner {
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private RedisCount redisCount;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 50; i++) {
            executorService.submit(new RedisCountThread(redisCount, ThreadLocalRandom.current().nextLong(1000)));
        }
        executorService.shutdown();
    }
}
