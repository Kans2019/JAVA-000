package org.geektime.redis.app;

import org.geektime.redis.support.RedisLock;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Component
public class RedisLockApplication implements ApplicationRunner, DisposableBean {
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private RedisLock redisLock;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String key = "test:lock:key";
        for (int i = 0; i < 50; i++) {
            executorService.submit(new RedisLockThread(redisLock, key));
            Thread.sleep(100L);
        }
        this.destroy();
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }
}
