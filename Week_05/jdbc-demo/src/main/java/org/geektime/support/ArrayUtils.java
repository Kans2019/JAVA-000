package org.geektime.support;

import java.util.Objects;

/**
 * 对数组处理的工具类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public interface ArrayUtils {
    /**
     * 判断两个数组是否相等
     *
     * {@code equals(null, {}) = true}
     * {@code equals({1}, {1}) = true}
     * {@code equals(null, {1}) = false}
     * {@code equals({2}, {1}) = false}
     * {@code equals({}, {1}) = false}
     *
     * @param objects1
     * @param objects2
     * @return
     */
    static boolean equals(Object[] objects1, Object[] objects2) {
        if (objects1 == objects2) return true;
        if (Objects.isNull(objects1) && objects2.length == 0 || Objects.isNull(objects2) && objects1.length == 0) {
            return true;
        }
        if (objects1.length != objects2.length) {
            return false;
        }

        boolean result = true;
        for (int i = 0; result && i < objects1.length; i++) {
            Object obj1 = objects1[i], obj2 = objects2[i];
            result = Objects.equals(obj1, obj2);
        }
        return result;
    }
}
