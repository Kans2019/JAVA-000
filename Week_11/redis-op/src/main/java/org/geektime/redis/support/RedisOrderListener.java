package org.geektime.redis.support;

import com.alibaba.fastjson.JSON;
import org.geektime.redis.demo.entity.Order;
import org.geektime.redis.demo.processor.OrderProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
public class RedisOrderListener implements MessageListener {
    private final static Logger logger = LoggerFactory.getLogger(RedisOrderListener.class);

    @Autowired
    private OrderProcessor orderProcessor;

    @Autowired
    private RedisSerializer<Order> orderRedisSerializer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("channel: {}, body: {}, pattern: {}", new String(message.getChannel()), new String(message.getBody()),
                new String(pattern));
        orderProcessor.process(orderRedisSerializer.deserialize(message.getBody()));
    }
}
