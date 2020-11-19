package org.geektime.support.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.geektime.support.cache.annotation.MyCache;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 处理缓存逻辑的AOP
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
@Aspect
@Component
public class CacheAopSupport {
    @Autowired
    private CacheStrategy cacheStrategy;


    @Around("cache()")
    public Object cacheAround(ProceedingJoinPoint proj) throws Throwable {
        MyCache myCache = null;
        Signature signature = proj.getSignature();
        if (signature instanceof MethodSignature) {
            myCache = ((MethodSignature) signature).getMethod().getAnnotation(MyCache.class);
        }
        if (Objects.isNull(myCache)) {
            myCache = (MyCache) proj.getSignature().getDeclaringType().getDeclaredAnnotation(MyCache.class);
        }
        String key = null;
        if (Objects.nonNull(myCache)) {
            key = this.parseKey(proj);
            try {
                return cacheStrategy.get(key);
            } catch (NoSuchElementException ignore) {
            }
        }
        Object value = proj.proceed();
        if (Objects.nonNull(myCache)) {
            this.cacheStrategy.put(key, value, myCache.value());
        }
        return value;
    }

    @Pointcut("(execution(public * org.geektime.service..*.*(..)))")
    public void cache() {}

    /**
     * 获取对应调用的key
     * @param proceedingJoinPoint
     * @return
     */
    private String parseKey(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder key = new StringBuilder();
        key.append(proceedingJoinPoint.getTarget()).append('.');
        key.append(proceedingJoinPoint.getSignature().toLongString());
        key.append('.').append(Arrays.toString(proceedingJoinPoint.getArgs()));
        return key.toString();
    }
}
