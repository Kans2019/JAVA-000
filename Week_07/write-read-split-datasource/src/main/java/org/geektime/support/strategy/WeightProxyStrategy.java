package org.geektime.support.strategy;

import org.geektime.support.WeightDataSource;

import java.util.List;
import java.util.Random;

/**
 * @author Terrdi
 * @description 权重代理策略
 * @date 2020/11/3
 */
public class WeightProxyStrategy<T extends WeightFul> implements ProxyStrategy<T> {
    private final Random random = new Random();

    @Override
    public T getNext(List<T> collection) {
        int sum = 0;
        for (T proxy : collection) {
            sum += proxy.getWeight();
        }
        if (sum == 0) {
            return collection.get(random.nextInt(collection.size()));
        } else {
            sum = random.nextInt(sum);
            for (T proxy : collection) {
                if (proxy.getWeight() > sum) {
                    return proxy;
                }
                sum -= proxy.getWeight();
            }
        }
        return collection.get(random.nextInt(collection.size()));
    }
}
