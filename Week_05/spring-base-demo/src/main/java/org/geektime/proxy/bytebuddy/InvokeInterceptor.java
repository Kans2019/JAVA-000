package org.geektime.proxy.bytebuddy;

import net.bytebuddy.asm.Advice;
import org.geektime.proxy.AfterHandler;
import org.geektime.proxy.BeforeHandler;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 执行拦截的类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public interface InvokeInterceptor {
    @SuppressWarnings("unchecked")
    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    static <T> boolean invokeBefore(@Advice.This T target, @Advice.Origin Method method, @Advice.AllArguments Object... argus) throws Throwable {
        boolean flag = true;
        Class<?> clazz = target.getClass();
        Deque<Class<?>> classes = new LinkedList<>();
        while (flag && Objects.nonNull(clazz)) {
            Interceptor<T> interceptor = (Interceptor<T>) Interceptor.getInstance(clazz);
            Iterator<BeforeHandler<T>> handlerIterator = interceptor.getBeforeHandlers().iterator();
            while (flag && handlerIterator.hasNext()) {
                BeforeHandler<T> beforeHandler = handlerIterator.next();
                flag = beforeHandler.beforeInvoke(target, method, argus);
            }
            classes.offerLast(clazz.getSuperclass());
            Arrays.stream(clazz.getInterfaces()).forEach(classes::offerLast);
            clazz = classes.pollFirst();
        }
        return flag;
    }

    @SuppressWarnings("unchecked")
    @Advice.OnMethodExit
    static <T> void invokeAfter(@Advice.This T target, @Advice.Origin Method method, @Advice.AllArguments Object... argus) throws Throwable {
        Class<?> clazz = target.getClass();
        Deque<Class<?>> classes = new LinkedList<>();
        while (Objects.nonNull(clazz)) {
            Interceptor<T> interceptor = (Interceptor<T>) Interceptor.getInstance(clazz);
            for (AfterHandler<T> afterHandler : interceptor.getAfterHandlers()) {
                afterHandler.afterInvoke(target, method, argus);
            }
            classes.offerLast(clazz.getSuperclass());
            Arrays.stream(clazz.getInterfaces()).forEach(classes::offerLast);
            clazz = classes.pollFirst();
        }
    }
}
