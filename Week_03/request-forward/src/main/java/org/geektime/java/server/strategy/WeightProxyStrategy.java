package org.geektime.java.server.strategy;

import org.geektime.java.server.Proxy;

import java.util.List;
import java.util.Random;

/**
 * @author Terrdi
 * @description 权重代理策略
 * @date 2020/11/3
 */
public class WeightProxyStrategy implements ProxyStrategy {
    private Random random = new Random();

    @Override
    public Proxy getNext(List<Proxy> collection) {
        int sum = 0;
        for (Proxy proxy : collection) {
            sum += proxy.getWeight();
        }
        if (sum == 0) {
            return collection.get(random.nextInt(collection.size()));
        } else {
            sum = random.nextInt(sum);
            for (Proxy proxy : collection) {
                if (proxy.getWeight() >= sum) {
                    return proxy;
                }
                sum -= proxy.getWeight();
            }
        }
        return collection.get(random.nextInt(collection.size()));
    }
}
