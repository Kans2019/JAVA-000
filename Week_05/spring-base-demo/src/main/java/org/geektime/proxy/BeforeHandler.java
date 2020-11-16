package org.geektime.proxy;

import java.lang.reflect.Method;

/**
 * 代理前置处理
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
@FunctionalInterface
public interface BeforeHandler<T> {
    /**
     * 在代理调用方法前调用
     * @param target 被代理的对象
     * @param method 被代理的方法
     * @param argus 被代理的方法的参数列表
     * @return true 继续调用 false返回空
     */
    boolean beforeInvoke(T target, Method method, Object... argus) throws Throwable;
}
