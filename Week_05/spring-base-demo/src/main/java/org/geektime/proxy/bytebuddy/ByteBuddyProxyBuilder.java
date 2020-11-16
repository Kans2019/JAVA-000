package org.geektime.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;
import org.geektime.proxy.ProxyBuilder;

/**
 * @author liuhanwei
 * @description
 * @date 2020/11/16
 */
public class ByteBuddyProxyBuilder<T> implements ProxyBuilder<T> {
    private final DynamicType.Builder<T> builder;

    public ByteBuddyProxyBuilder(T target) {
        this.builder = (DynamicType.Builder<T>) new ByteBuddy()
                .with(new NamingStrategy.SuffixingRandom("suffix"))
                .subclass(target.getClass());
    }

    @Override
    public T build() {
        try {
            return this.builder.make().load(this.getClass().getClassLoader()).getLoaded().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassCastException("生成代理对象失败 " + e.getMessage());
        }
    }

    @Override
    public ProxyBuilder<T> registerBeforeHandler(BeforeHandler<T> beforeHandler) {
        return null;
    }

    @Override
    public ProxyBuilder<T> registerAfterHandler(AfterHandler<T> afterHandler) {
        return null;
    }
}
