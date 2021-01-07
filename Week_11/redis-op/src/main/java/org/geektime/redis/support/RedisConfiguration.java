package org.geektime.redis.support;

import org.geektime.redis.demo.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
@Configuration
public class RedisConfiguration {
    /**
     * 模拟减库存的数量
     */
    @Value("${geektime.stock:10000}")
    private long stock;

    @Bean
    public RedisTemplate<String, Long> stringLongRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new RedisSerializer<Long>() {
            @Override
            public byte[] serialize(Long aLong) throws SerializationException {
                return stringRedisSerializer.serialize(aLong.toString());
            }

            @Override
            public Long deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null) {
                    return null;
                }
                return Long.parseLong(Objects.requireNonNull(stringRedisSerializer.deserialize(bytes)));
            }
        });
        return redisTemplate;
    }

    @Bean
    @ConditionalOnBean(name = "stringLongRedisTemplate")
    public RedisCount redisCount() {
        return new RedisCount(stock);
    }

    @Bean
    public Topic orderTopic() {
        return new ChannelTopic("test:order");
    }

    @Bean
    public RedisOrderListener orderListener() {
        return new RedisOrderListener();
    }

    @Bean
    public RedisSerializer<Order> orderRedisSerializer() {
        return new com.alibaba.fastjson.support.spring.FastJsonRedisSerializer<>(Order.class);
    }

    @Bean("stringOrderRedisTemplate")
    public RedisTemplate<String, Order> stringOrderRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Order> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(orderRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @ConditionalOnBean(name = "stringOrderRedisTemplate")
    public RedisMessageListenerContainer redisMessageListenerContainer(LettuceConnectionFactory connectionFactory, RedisOrderListener orderListener) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(orderListener, orderTopic());

        return container;
    }
}
