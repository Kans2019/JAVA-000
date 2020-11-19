package org.geektime.support.cache.impl;

import org.geektime.support.cache.CacheStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存的自动配置策略加载
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
@Configuration
public class CacheStrategyAutoConfiguration {
    @Bean
    @ConditionalOnExpression
    public CacheStrategy guavaCacheStrategy() {
        return new GuavaCacheStrategy();
    }
}
