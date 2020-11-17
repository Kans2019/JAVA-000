package org.geektime.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;
import org.geektime.proxy.ProxyBuilder;

/**
 * {@link net.bytebuddy.ByteBuddy} 实现AOP
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public class ByteBuddyProxyBuilder<T> implements ProxyBuilder<T> {
    /**
     * 对指定类进行拦截
     */
    private final Class<T> clazz;

    /**
     * 拦截器
     */
    private final Interceptor<T> interceptor;

    @SuppressWarnings("unchecked")
    public ByteBuddyProxyBuilder(T target) {
        DynamicType.Builder<T> builder = (DynamicType.Builder<T>) new ByteBuddy()
                .with(new NamingStrategy.SuffixingRandom("suffix"))
                .subclass(target.getClass())
                .method(ElementMatchers.isPublic())
                .intercept(Advice.to(InvokeInterceptor.class));
        this.clazz = (Class<T>) builder.make().load(this.getClass().getClassLoader()).getLoaded();
        this.interceptor = (Interceptor<T>) Interceptor.getInstance(this.clazz);
    }

    @Override
    public T build() {
        try {
            return this.clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassCastException("生成代理对象失败 " + e.getMessage());
        }
    }

    @Override
    public ProxyBuilder<T> registerBeforeHandler(BeforeHandler<T> beforeHandler) {
        interceptor.add(beforeHandler);
        return this;
    }

    @Override
    public ProxyBuilder<T> registerAfterHandler(AfterHandler<T> afterHandler) {
        interceptor.add(afterHandler);
        return this;
    }
}
