package org.geektime.batchinsert.pojo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component
public class OrderList extends ArrayList<Order> implements List<Order>, InitializingBean {
    private final int BATCH = 1000000;

    protected Order generate() {
        Random random = new Random();
        return new Order(null, random.nextLong(), random.nextInt(),
                random.nextInt(), BigDecimal.valueOf(random.nextLong() & 0xFFFFFF), random.nextInt(4),
                random.nextInt(10), random.nextInt(4), new Date(), new Date(), new Date(), new Date(), new Date(), new Date());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (long i = 0; i < BATCH; i++) {
            this.add(generate());
        }
    }
}
