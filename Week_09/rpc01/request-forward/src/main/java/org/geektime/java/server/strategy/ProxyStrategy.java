package org.geektime.java.server.strategy;

import org.geektime.java.server.Proxy;

import java.util.*;

/**
 * @author Terrdi
 * @description 代理策略
 * @date 2020/11/3
 */
public interface ProxyStrategy {
    Map<String, ProxyStrategy> strategyTable = Collections.unmodifiableMap(new HashMap<String, ProxyStrategy> () {
        {
            this.put("random", new RandomProxyStrategy());
            this.put("roundrobin", new RoundRobinProxyStrategy());
            this.put("weight", new WeightProxyStrategy());
        }
    });


    /**
     * 获取下一次访问的代理地址
     * @param collection
     * @return
     */
    Proxy getNext(List<Proxy> collection);
}
