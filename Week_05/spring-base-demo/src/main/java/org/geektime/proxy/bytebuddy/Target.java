package org.geektime.proxy.bytebuddy;


/**
 * 被代理的类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public class Target {
    public void sayHello() {
        System.out.println("Hello World!");
    }
}