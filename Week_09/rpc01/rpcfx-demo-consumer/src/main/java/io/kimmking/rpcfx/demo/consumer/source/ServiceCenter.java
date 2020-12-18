package io.kimmking.rpcfx.demo.consumer.source;

import io.kimmking.rpcfx.aop.ByteBuddyFactory;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 **/
@Configuration
public class ServiceCenter {
    @Autowired
    private ByteBuddyFactory byteBuddyFactory;

    @Bean
    public UserService userService() {
        return byteBuddyFactory.createRpcfx(UserService.class);
    }

    @Bean
    public OrderService orderService() {
        return byteBuddyFactory.createRpcfx(OrderService.class);
    }
}
