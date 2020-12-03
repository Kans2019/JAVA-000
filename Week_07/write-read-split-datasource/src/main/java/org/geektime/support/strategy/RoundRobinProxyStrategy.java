package org.geektime.support.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description 轮询代理策略
 * @date 2020/11/3
 */
public class RoundRobinProxyStrategy<T> implements ProxyStrategy<T> {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public T getNext(List<T> collection) {
        if (atomicInteger.get() >= collection.size()) {
            atomicInteger.set(0);
        }
        return collection.get(atomicInteger.getAndIncrement());
    }
}
