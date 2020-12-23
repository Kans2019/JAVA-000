package org.geektime.consumer;

import org.dromara.hmily.core.service.HmilyApplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/21
 * @since 1.8
 **/
@SpringBootApplication
public class DubboHmilyConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboHmilyConsumerApplication.class, args);
    }

    @Bean
    public HmilyApplicationService hmilyApplicationService() {
        return () -> "dubbo-hmily-demo-consumer";
    }
}
