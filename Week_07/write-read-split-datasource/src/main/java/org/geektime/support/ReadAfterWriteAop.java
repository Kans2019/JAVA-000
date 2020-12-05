package org.geektime.support;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.geektime.common.DataSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 写完读
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Profile("jdbc")
@Aspect
@Component
public class ReadAfterWriteAop {
    @Pointcut("(execution(public * org.geektime.service..*.*(..)))")
    public void service(){}

    @Autowired
    private DynamicDataSource dynamicDataSource;

    // 通过CacheBuilder构建一个缓存实例
    private final LoadingCache<String, Optional> cache = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大容量
            .expireAfterWrite(10, TimeUnit.SECONDS) // 假设10s后主从同步完毕
            .concurrencyLevel(10) // 设置并发级别为10
            .recordStats() // 开启缓存统计
            .build(new CacheLoader<String, Optional>() {
                @Override
                public Optional load(String key) throws Exception {
                    return Optional.empty();
                }
            });

    @Around("service()")
    public Object readWriteSplit(ProceedingJoinPoint proj) throws Throwable {
        boolean flag = false;
        final String key = this.getSessionId();
        DataSourceOperation original = (DataSourceOperation) dynamicDataSource.determineCurrentLookupKey();
        if (cache.get(key).isPresent()) {
            dynamicDataSource.setCurrentDataSourceId(DataSourceOperation.WRITE);
            flag = true;
        }
        if (Objects.equals(DataSourceOperation.WRITE, original)) {
            if (flag) {
                cache.refresh(key);
            } else {
                cache.put(key, Optional.of(0));
            }
        }
        return proj.proceed();
    }

    public String getSessionId() {
        // 在线上换成实际的用户会话id
        return "test";
    }
}
