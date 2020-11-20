package org.geektime.support.cache.annotation;

import java.lang.annotation.*;

/**
 * 自定义缓存
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 * @see org.geektime.support.cache.CacheAopSupport
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCache {
    /**
     * 配置缓存时间
     * 单位 秒
     * @return
     */
    int value() default 60;
}
