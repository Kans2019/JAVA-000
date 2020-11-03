package org.geektime.java.server;

import java.util.List;

/**
 * @author Terrdi
 * @description
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
