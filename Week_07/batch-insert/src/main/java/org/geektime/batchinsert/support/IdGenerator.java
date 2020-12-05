package org.geektime.batchinsert.support;

/**
 * 生成id的生成器
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@FunctionalInterface
public interface IdGenerator<T> {
    /**
     * 获取id
     * @return
     */
    T generate();
}
