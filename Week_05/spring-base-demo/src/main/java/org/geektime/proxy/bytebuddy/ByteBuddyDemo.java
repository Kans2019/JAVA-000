package org.geektime.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

/**
 * {@link net.bytebuddy.ByteBuddy} 使用Demo
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/16
 * @since 1.8
 **/
public class ByteBuddyDemo {
    public static void main(String[] args) throws ReflectiveOperationException {
        DynamicType.Unloaded<Target> dynamicType = new ByteBuddy().subclass(Target.class)
                .name("org.geektime.proxy.bytebuddy.Target$ByteBuddy")
                .make();

        Class<? extends Target> clazz = dynamicType.load(Target.class.getClassLoader())
                .getLoaded();

        Target obj = clazz.newInstance();
        obj.sayHello();
    }
}
