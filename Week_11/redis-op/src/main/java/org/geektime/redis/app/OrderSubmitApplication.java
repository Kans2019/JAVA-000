package org.geektime.redis.app;

import org.geektime.redis.demo.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 提交任务的应用类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Component
public class OrderSubmitApplication implements ApplicationRunner {
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private RedisTemplate<String, Order> redisTemplate;

    @Autowired
    private Topic orderTopic;

    @Value("${geektime.price.unit:10}")
    private BigDecimal unitPrice;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0;i < 500;i++) {
            executorService.submit(() -> {
                Order order = new Order();
                order.setId(UUID.randomUUID().toString());
                order.setBuyer("buyer-" + ThreadLocalRandom.current().nextInt(10));
                long count = ThreadLocalRandom.current().nextInt(100);
                order.setCount(count);
                order.setPrice(unitPrice.multiply(BigDecimal.valueOf(count)));
                order.setProduct("product-" + ThreadLocalRandom.current().nextInt(100));
                redisTemplate.convertAndSend(orderTopic.getTopic(), order);
            });
        }
        executorService.shutdown();
    }
}
