package org.geektime.support.strategy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 对读进行负载均衡策略的配置类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/3
 * @since 1.8
 **/
@Configuration
public class ProxyStrategyConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "geektime.read", name = "strategy", havingValue = "random", matchIfMissing = true)
    public ProxyStrategy<DataSource> randomProxyStrategy() {
        return new RandomProxyStrategy<>();
    }

    @Bean
    @ConditionalOnProperty(prefix = "geektime.read", name = "strategy", havingValue = "round-robin")
    public ProxyStrategy<DataSource> roundRobinProxyStrategy() {
        return new RoundRobinProxyStrategy<>();
    }

    @Bean
    @ConditionalOnProperty(prefix = "geektime.read", name = "strategy", havingValue = "weight")
    public ProxyStrategy<WeightDataSource> weightProxyStrategy() {
        return new WeightProxyStrategy<>();
    }
}
