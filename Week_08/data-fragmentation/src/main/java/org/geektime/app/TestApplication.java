package org.geektime.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.geektime.pojo.Order;
import org.geektime.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/8
 * @since 1.8
 **/
@Component
public class TestApplication implements ApplicationRunner {
    @Autowired
    private IOrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        orderService.remove(new QueryWrapper<>());
//        try {
//            Order order = new Order();
//            order.setId(1);
//            order.setUserId(-1234231);
//            order.setOrderNo(112312312L);
//            orderService.save(order);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Order order = new Order();
//            order.setId(1);
//            order.setUserId(1234231);
//            order.setOrderNo(-112312312L);
//            orderService.save(order);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < 10; i++) {
            orderService.save(generate());
        }

        orderService.list().forEach(System.out::println);
    }

    protected Order generate() {
        Random random = new Random();
        return new Order(random.nextInt(3000), Math.abs(random.nextLong()), Math.abs(random.nextInt()),
                random.nextInt(), BigDecimal.valueOf(random.nextLong() & 0xFFFFFF), random.nextInt(4),
                random.nextInt(10), random.nextInt(4), new Date(), new Date(), new Date(), new Date(), new Date(), new Date());
    }
}
