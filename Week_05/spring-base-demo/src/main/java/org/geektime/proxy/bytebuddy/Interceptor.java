package org.geektime.proxy.bytebuddy;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对{@link ByteBuddyProxyBuilder}的代理对象进行Aop拦截的类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public class Interceptor<T> {
    /**
     * 前置处理器集合
     */
    private LinkedList<BeforeHandler<T>> beforeHandlers = new LinkedList<>();

    /**
     * 后置处理器集合
     */
    private LinkedList<AfterHandler<T>> afterHandlers = new LinkedList<>();

    public List<BeforeHandler<T>> getBeforeHandlers() {
        return Collections.unmodifiableList(beforeHandlers);
    }

    public List<AfterHandler<T>> getAfterHandlers() {
        return Collections.unmodifiableList(afterHandlers);
    }

    /**
     * 添加前置处理器
     * @param beforeHandler
     */
    public void add(BeforeHandler<T> beforeHandler) {
        this.beforeHandlers.addFirst(beforeHandler);
    }

    /**
     * 添加后置处理器
     * @param afterHandler
     */
    public void add(AfterHandler<T> afterHandler) {
        this.afterHandlers.add(afterHandler);
    }

    private Interceptor() {}

    private final static Map<Class<?>, Interceptor<?>> map = new ConcurrentHashMap<>();

    
}
