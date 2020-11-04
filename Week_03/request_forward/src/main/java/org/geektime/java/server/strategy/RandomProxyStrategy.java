package org.geektime.java.server.strategy;

import org.geektime.java.server.Proxy;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Terrdi
 * @description 随机代理策略
 * @date 2020/11/3
 */
public class RandomProxyStrategy implements ProxyStrategy {
    private Random random = new Random();

    @Override
    public Proxy getNext(List<Proxy> collection) {
        return collection.get(random.nextInt(collection.size()));
    }
}
