package org.geektime.mq.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/17
 * @since 1.8
 **/
@Component
public class Producer implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(Producer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 1;i < 1000;i++)
            kafkaTemplate.send("test", "message", String.format("第%d条消息", i)).addCallback(success -> {
                logger.info("发送消息成功");
            }, failure -> logger.info("发送消息失败 ", failure));
    }
}
