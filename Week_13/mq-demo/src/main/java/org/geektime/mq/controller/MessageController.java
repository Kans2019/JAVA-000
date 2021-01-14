package org.geektime.mq.controller;

import org.geektime.mq.entity.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 方便外部调用发送消息
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/8
 * @since 1.8
 **/
@RestController
public class MessageController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/message")
    public String post(@RequestBody MessageVo messageVo) {
        jmsTemplate.convertAndSend("test", messageVo);
        return "success";
    }
}
