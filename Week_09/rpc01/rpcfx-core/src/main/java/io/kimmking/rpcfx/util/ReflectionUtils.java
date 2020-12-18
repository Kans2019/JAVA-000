package io.kimmking.rpcfx.util;

import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 基于{@link org.springframework.util.ReflectionUtils}反射工具类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 * @see org.springframework.util.ReflectionUtils
 **/
public interface ReflectionUtils {
    static Method findMethod(Class<?> originalClazz, String methodName, Class<?>... classes) {
        Assert.notNull(originalClazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        Class<?> searchType = originalClazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == classes.length) {
                        boolean flag = true;
                        for (int i = 0; flag && i < paramTypes.length; i++) {
                            flag = isAssignForm(paramTypes[i], classes[i]);
                        }
                        return method;
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    static boolean isAssignForm(Class<?> target, Class<?> source) {
        if (target.isAssignableFrom(source)) {
            return true;
        }
        try {
            if (target.isPrimitive() || source.isPrimitive()) {
                target = source.isPrimitive() ? (Class<?>) org.springframework.util.ReflectionUtils.getField(
                        Objects.requireNonNull(org.springframework.util.ReflectionUtils.findField(target, "TYPE", Class.class)), null) : target;
                source = !source.isPrimitive() && Objects.requireNonNull(target).isPrimitive() ? (Class<?>) org.springframework.util.ReflectionUtils.getField(
                        Objects.requireNonNull(org.springframework.util.ReflectionUtils.findField(source, "TYPE", Class.class)), null) : source;
                return Objects.requireNonNull(target).isAssignableFrom(Objects.requireNonNull(source));
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
}
