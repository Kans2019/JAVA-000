package org.geektime.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 动态代理抽象类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 * @see net.sf.cglib.proxy.MethodInterceptor
 **/
public class CglibProxyBuilder<T> implements net.sf.cglib.proxy.MethodInterceptor {
    private final Enhancer enhancer = new Enhancer();

    public CglibProxyBuilder(T target) {
        this.enhancer.setSuperclass(target.getClass());
        this.enhancer.setCallback(this);
    }

    /**
     * 获取相应的代理对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public synchronized T build() {
        // 将前置方法调用反序 使最后注册的最先调用
        Collections.reverse(this.beforeHandlers);
        this.beforeHandlers = Collections.unmodifiableList(this.beforeHandlers);
        this.afterHandlers = Collections.unmodifiableList(this.afterHandlers);
        return (T) this.enhancer.create();
    }

    private List<BeforeHandler<T>> beforeHandlers = new LinkedList<>();

    private List<AfterHandler<T>> afterHandlers = new LinkedList<>();

    /**
     * 在最前面注册前置方法
     * @param beforeHandler 前置方法调用
     * @return
     */
    public CglibProxyBuilder<T> registerBeforeHandler(BeforeHandler<T> beforeHandler) {
        this.beforeHandlers.add(Objects.requireNonNull(beforeHandler, "不允许不存在的方法"));
        return this;
    }

    /**
     * 在最后面注册后置方法
     * @param afterHandler 后置方法调用
     * @return
     */
    public CglibProxyBuilder<T> registerAfterHandler(AfterHandler<T> afterHandler) {
        this.afterHandlers.add(Objects.requireNonNull(afterHandler, "不允许不存在的方法"));
        return this;
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
        T target = this.handle(obj);
        boolean flag = Objects.nonNull(target);
        Object object = null;
        Iterator<BeforeHandler<T>> beforeHandlerIterator = this.beforeHandlers.iterator();
        while (flag && beforeHandlerIterator.hasNext()) {
            BeforeHandler<T> beforeHandler = beforeHandlerIterator.next();
            flag = beforeHandler.beforeInvoke(target, method, args);
        }
        if (flag) {
            object = proxy.invokeSuper(obj, args);
            for (AfterHandler<T> handler : this.afterHandlers) {
                handler.afterInvoke(target, method, args);
            }
        }

        return object;
    }

    /**
     * 将代理对象转换成当前对象可以处理的对象
     * @param obj 代理对象
     * @return 可以处理的对象/ {@code null}代表不能处理的对象
     */
    private T handle(Object obj) {
        try {
            return (T) obj;
        } catch (ClassCastException ignore) {
            return null;
        }
    }
}
