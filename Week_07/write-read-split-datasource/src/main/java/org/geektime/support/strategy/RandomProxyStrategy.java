package org.geektime.support.strategy;

import java.util.List;
import java.util.Random;

/**
 * @author Terrdi
 * @description 随机代理策略
 * @date 2020/11/3
 */
public class RandomProxyStrategy<T> implements ProxyStrategy<T> {
    private Random random = new Random();

    @Override
    public T getNext(List<T> collection) {
        return collection.get(random.nextInt(collection.size()));
    }
}
