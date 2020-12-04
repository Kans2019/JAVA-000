package org.geektime.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.geektime.common.DataSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Around("service() && @annotation(ReadOnly)")
    public Object readWriteSplit(ProceedingJoinPoint proj) throws Throwable {
        dynamicDataSource.setCurrentDataSourceId(DataSourceOperation.READ);
        try {
            return proj.proceed();
        } finally {
            dynamicDataSource.removeCurrentDataSourceId();
        }
    }
}
