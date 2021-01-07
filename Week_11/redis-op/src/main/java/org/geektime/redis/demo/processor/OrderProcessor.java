package org.geektime.redis.demo.processor;

import org.geektime.redis.demo.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单处理
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Component
public class OrderProcessor {
    private final static Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    public void process(Order order) {
        logger.info("用户[{}]购买了[{}]{}个, 花费了{}元", order.getBuyer(), order.getProduct(), order.getCount(), order.getPrice());
    }
}
