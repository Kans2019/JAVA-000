package io.kimmking.rpcfx.aop;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 **/
public class ObjectBuddyFactory {
    @SuppressWarnings("unchecked")
    @RuntimeType
    @Advice.OnMethodEnter
    public static Object intercept(@SuperCall Callable<?> callable) throws Throwable {
        return callable.call();
    }
}
