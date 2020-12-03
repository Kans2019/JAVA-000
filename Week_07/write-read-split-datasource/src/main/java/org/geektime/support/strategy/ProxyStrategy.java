package org.geektime.support.strategy;

import java.util.List;

/**
 * @author Terrdi
 * @description 代理策略
 * @date 2020/11/3
 */
public interface ProxyStrategy<T> {
    /**
     * 获取下一次访问的代理地址
     * @param collection
     * @return
     */
    T getNext(List<T> collection);
}
