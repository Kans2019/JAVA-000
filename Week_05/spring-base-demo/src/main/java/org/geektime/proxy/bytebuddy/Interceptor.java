package org.geektime.proxy.bytebuddy;

import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义对各个类进行拦截的方法
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public class Interceptor<T> {
    /**
     * 前置处理器集合
     */
    private final LinkedList<BeforeHandler<T>> beforeHandlers = new LinkedList<>();

    /**
     * 后置处理器集合
     */
    private final LinkedList<AfterHandler<T>> afterHandlers = new LinkedList<>();

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

    /**
     * 获取指定类的拦截器
     * @param clazz
     * @return
     */
    public static Interceptor<?> getInstance(Class<?> clazz) {
        if (!map.containsKey(clazz)) {
            map.put(clazz, new Interceptor<>());
        }
        return map.get(clazz);
    }
}
