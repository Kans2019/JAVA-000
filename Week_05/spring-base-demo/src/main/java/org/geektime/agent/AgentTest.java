package org.geektime.agent;

import org.geektime.support.InvokeTimeAop;

/**
 * javaagent 测试类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 * @see InvokeTimeAop
 * @see AgentDemo
 **/
public class AgentTest {
    static {
        InvokeTimeAop.registerClass(AgentTest.class);
    }

    public void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sayHello() {
        System.out.println("hello!!!");
    }
}
