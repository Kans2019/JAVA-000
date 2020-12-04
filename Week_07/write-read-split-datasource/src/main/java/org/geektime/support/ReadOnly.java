package org.geektime.support;

import java.lang.annotation.*;

/**
 * 使用读数据源
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 * @see DynamicDataSourceAop
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ReadOnly {
}
