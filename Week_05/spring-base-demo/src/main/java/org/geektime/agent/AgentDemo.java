package org.geektime.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * javaagent 练习demo
 * ``` mvn clean package -Dmaven.test.skip=true ```
 * 打包后配合JVM参数使用
 * -javaagent:target/agent-demo.jar
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public class AgentDemo {
    public static void premain(String agentArgs) {
        System.out.println("premain...");
        System.out.println("agentArgs = " + agentArgs);
        System.out.println("--------------------------\n");
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        // 添加 Transformer
        ClassFileTransformer transformer = new AopClassFileTransformer();
        inst.addTransformer(transformer);
    }

    public static void main(String[] args) {
        AgentTest agentTest = new AgentTest();
        agentTest.sleep(1000);
        agentTest.sayHello();
    }
}
