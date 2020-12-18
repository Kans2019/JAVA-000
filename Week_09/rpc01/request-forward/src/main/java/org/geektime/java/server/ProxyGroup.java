package org.geektime.java.server;

import java.util.List;

/**
 * @author Terrdi
 * @description 负载均衡pojo 与 {@link org.geektime.java.common.Constant#PROXY_XML} 里的 proxy 一一对应
 * @date 2020/11/3
 */
public class ProxyGroup {
    private final List<Proxy> collection;

    private final String strategy;

    public ProxyGroup(List<Proxy> collection, String strategy) {
        this.collection = collection;
        this.strategy = strategy;
    }

    public List<Proxy> getCollection() {
        return collection;
    }

    public String getStrategy() {
        return strategy;
    }
}
