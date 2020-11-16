package org.geektime.proxy;

/**
 * 代理类构造器接口
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public interface ProxyBuilder<T> {
    /**
     * 获取代理的对象
     * @return
     */
    T build();

    /**
     * 在最前面注册前置方法
     * @param beforeHandler 前置方法调用
     * @return
     */
    ProxyBuilder<T> registerBeforeHandler(BeforeHandler<T> beforeHandler);

    /**
     * 在最后面注册后置方法
     * @param afterHandler 后置方法调用
     * @return
     */
    ProxyBuilder<T> registerAfterHandler(AfterHandler<T> afterHandler);
}
