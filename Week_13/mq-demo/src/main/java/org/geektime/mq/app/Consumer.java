package org.geektime.mq.app;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/17
 * @since 1.8
 **/
@Component
public class Consumer {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "test")
    public void onMessage(ConsumerRecord<?, ?> consumerRecord) {
        logger.info("消费到 {} 的消息 {}:{}", consumerRecord.topic(), consumerRecord.key(), consumerRecord.value());
    }
}
