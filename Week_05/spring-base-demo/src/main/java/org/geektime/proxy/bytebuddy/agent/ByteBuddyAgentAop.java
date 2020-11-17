package org.geektime.proxy.bytebuddy.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.geektime.proxy.bytebuddy.InvokeInterceptor;

import java.lang.instrument.Instrumentation;

/**
 * java-agent 配合 ByteBuddy 进行 Aop 拦截
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 * @see org.geektime.proxy.bytebuddy.InvokeInterceptor
 * @see <a href="https://blog.csdn.net/f59130/article/details/78494572">使用ByteBuddy实现一个Java-Agent</a>
 */
public class ByteBuddyAgentAop {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, module) ->
                builder.method(ElementMatchers.isPublic()).intercept(Advice.to(InvokeInterceptor.class));

        AgentBuilder.Listener listener = AgentBuilder.Listener.NoOp.INSTANCE;

        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWithIgnoreCase("org.geektime"))
                .transform(transformer).with(listener).installOn(instrumentation);
    }
}
