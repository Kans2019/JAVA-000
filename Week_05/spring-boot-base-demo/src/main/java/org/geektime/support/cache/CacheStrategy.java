package org.geektime.support.cache;

import java.util.NoSuchElementException;

/**
 * 缓存策略接口
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
public interface CacheStrategy {
    /**
     * 向缓存中添加一份数据
     * @param key
     * @param value
     * @param timeout 失效时间 单位ms
     */
    void put(String key, Object value, long timeout);

    default void put(String key, Object value) {
        this.put(key, value, this.timeout());
    }

    /**
     * 缓存失效时间(单位:ms)
     * @return
     */
    default long timeout() {
        return 1000;
    }

    /**
     * 从缓存中获取数据
     * @param key
     * @return
     * @throws NoSuchElementException 缓存不存在时, 抛出该异常
     */
    Object get(String key) throws NoSuchElementException;
}
