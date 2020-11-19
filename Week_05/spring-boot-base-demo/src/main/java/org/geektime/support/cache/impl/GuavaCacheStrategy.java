package org.geektime.support.cache.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.apache.log4j.Logger;
import org.geektime.support.cache.CacheStrategy;

import javax.annotation.PreDestroy;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 使用Guava实现缓存
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
public class GuavaCacheStrategy implements CacheStrategy, AutoCloseable {
    private static final Logger logger = Logger.getLogger(GuavaCacheStrategy.class);

    // 通过CacheBuilder构建一个缓存实例
    private final LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大容量
            .concurrencyLevel(10) // 设置并发级别为10
            .recordStats() // 开启缓存统计
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    throw new NoSuchElementException(key + " not exists.");
                }
            });

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Math.max(Runtime.getRuntime().availableProcessors() >> 1, 1));

    @Override
    public void put(String key, Object value, long timeout) {
        this.cache.put(key, value);
        // 使用线程池来使key失效
        executorService.schedule(() -> this.cache.invalidate(key),
                timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) throws NoSuchElementException {
        final Object value;
        try {
            value = this.cache.get(key);
            if (!clazz.isAssignableFrom(value.getClass())) {
                throw new NoSuchElementException(key + " not exists.");
            }
        } catch (ExecutionException | UncheckedExecutionException e) {
            throw new NoSuchElementException(key + " not exists.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("从缓存中取到 " + key + " ===> " + value);
        }
        return (T) value;
    }

    /**
     * 关闭所有资源
     */
    @Override
    @PreDestroy
    public void close() {
        this.executorService.shutdownNow();
        this.cache.invalidateAll();
    }
}
