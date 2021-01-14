package org.geektime.mq.service;

import org.geektime.mq.entity.MessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/8
 * @since 1.8
 **/
@Service
public class MessageHandler {
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @JmsListener(destination = "test", containerFactory = "myFactory")
    public void handle(MessageVo messageVo) {
        logger.info("接收到消息: {}", messageVo.getBody());
    }
}
