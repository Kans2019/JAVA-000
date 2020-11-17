package org.geektime.support;

import org.apache.log4j.Logger;
import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;
import org.geektime.proxy.bytebuddy.Interceptor;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 对方法的调用时间进行打印输出
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public class InvokeTimeAop {
    private static final Logger logger = Logger.getLogger(InvokeTimeAop.class);

    /**
     * 调用时钟量度
     */
    private final static AtomicLong clock = new AtomicLong(-1);

    private final static BeforeHandler<?> beforeHandler = (target, method, argus) -> {
        long start = System.currentTimeMillis();
        // 尝试拿锁
        while (clock.compareAndSet(-1, start));
        return true;
    };

    private final static AfterHandler<?> afterHandler = (target, method, argus) -> {
        long start = clock.getAndSet(-1);
        long end = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("%s#%s%s的调用时间为 %dms",
                    target.getClass().getTypeName(), method.getName(), Arrays.toString(argus), end - start));
        }
    };

    @SuppressWarnings("unchecked")
    public static synchronized <T> void registerClass(Class<T> clazz) {
        Interceptor<T> interceptor = (Interceptor<T>) Interceptor.getInstance(clazz);
        if (!interceptor.getBeforeHandlers().contains(beforeHandler)) {
            interceptor.add((BeforeHandler<T>) beforeHandler);
            interceptor.add((AfterHandler<T>) afterHandler);
        }
    }
}
