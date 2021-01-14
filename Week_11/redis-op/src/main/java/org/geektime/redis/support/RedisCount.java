package org.geektime.redis.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * 分布式计数器
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2021/1/7
 * @since 1.8
 **/
public class RedisCount implements InitializingBean, DisposableBean {
    private final static Logger logger = LoggerFactory.getLogger(RedisCount.class);


    private final long stock;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    private String key;

    public RedisCount(long stock) {
        this.stock = stock;
    }

    /**
     * 减库存
     * @param stock 减库存的数量
     * @return {@code true} 减库存成功
     */
    public boolean desc(long stock) {
        long leave = Optional.ofNullable(redisTemplate.opsForValue().increment(key, -stock)).orElse(-1L);
        if (leave < 0) {
            logger.info("减库存 [{}] 失败, 剩余库存 {}", stock, stock + leave);
            redisTemplate.opsForValue().increment(key, stock);
            return false;
        } else {
            logger.info("剩余库存 {}", leave);
            return true;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = this.toString();
        this.redisTemplate.opsForValue().set(this.key, this.stock);
    }

    @Override
    public void destroy() throws Exception {
        this.redisTemplate.delete(this.key);
    }
}
