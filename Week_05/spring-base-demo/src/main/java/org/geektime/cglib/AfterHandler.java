package org.geektime.cglib;

import java.lang.reflect.Method;

/**
 * 代理后置处理
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
@FunctionalInterface
public interface AfterHandler<T> {
    /**
     * 代理处理后调用的方法
     * @param target 被代理的对象
     * @param method 被代理的方法
     * @param argus 被代理的方法的参数列表
     */
    void afterInvoke(T target, Method method, Object... argus);
}
