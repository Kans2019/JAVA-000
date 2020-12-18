package org.geektime.java.server.strategy;

import org.geektime.java.server.Proxy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description 轮询代理策略
 * @date 2020/11/3
 */
public class RoundRobinProxyStrategy implements ProxyStrategy {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Proxy getNext(List<Proxy> collection) {
        if (atomicInteger.get() >= collection.size()) {
            atomicInteger.set(0);
        }
        return collection.get(atomicInteger.getAndIncrement());
    }
}
