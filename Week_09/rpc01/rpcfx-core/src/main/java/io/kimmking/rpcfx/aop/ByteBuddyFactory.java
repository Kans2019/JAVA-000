package io.kimmking.rpcfx.aop;

import io.kimmking.rpcfx.client.RpcfxInvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 使用byteBuddy的字节码生产器
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 **/
@Component
public class ByteBuddyFactory {
    @Value("${geektime.rpc.url:http://localhost:8080/}")
    private String url;

    public <T> T createRpcfx(Class<T> clazz) {
        DynamicType.Builder dynamicBuilder = new ByteBuddy().subclass(Object.class).implement(clazz)
                .name(clazz.getName() + "$ByteBuddy")
                .method(ElementMatchers.isDeclaredBy(Object.class)).intercept(SuperMethodCall.INSTANCE)
                .method(ElementMatchers.isOverriddenFrom(clazz)).intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(clazz, url)));
//        for (Method method : clazz.getMethods()) {
//            dynamicBuilder = dynamicBuilder.method(ElementMatchers.named(method.getName())).intercept(InvocationHandlerAdapter.of(new Rpcfx.RpcfxInvocationHandler(clazz, url)));
//        }
        DynamicType.Unloaded<T> dynamicType = dynamicBuilder.make();

        Class<? extends T> chClazz = dynamicType.load(ByteBuddyFactory.class.getClassLoader())
                .getLoaded();

        try {
            return chClazz.newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
