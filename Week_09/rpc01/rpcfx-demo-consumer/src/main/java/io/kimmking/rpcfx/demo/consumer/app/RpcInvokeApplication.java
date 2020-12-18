package io.kimmking.rpcfx.demo.consumer.app;

import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 **/
@Component
public class RpcInvokeApplication implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());

        Order order = orderService.findOrderById(1992129);
        System.out.printf("find order name=%s, amount=%f%n",order.getName(),order.getAmount());
    }
}
