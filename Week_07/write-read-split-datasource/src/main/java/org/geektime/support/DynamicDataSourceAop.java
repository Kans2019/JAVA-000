package org.geektime.support;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/3
 * @since 1.8
 **/
@Aspect
@Component
public class DynamicDataSourceAop {
    @Pointcut("(execution(public * org.geektime.service..*.*(..)))")
    public void service(){}
}
