package org.geektime.batchinsert.support;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component
public class IntegerIncrementGenerator implements IdGenerator<Integer> {
    AtomicInteger value = new AtomicInteger(1);

    @Override
    public Integer generate() {
        return value.getAndIncrement();
    }
}
