package org.geektime.support;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 如果该属性在Spring元数据中有定义,则加载该Bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ConditionalOnPropertyExistsCondition.class)
public @interface ConditionalOnPropertyExists {
    /**
     * 判断是否存在的属性名
     */
    String name() default "java.version";
}
