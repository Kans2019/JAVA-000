package org.geektime;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/10
 */
public interface SynchronizedRun<T> {
    /**
     * 执行任务获取结果
     * @return
     * @throws InterruptedException 发生中断异常
     */
    T execute() throws InterruptedException;

    default void print() throws InterruptedException {
        System.out.printf("%s 执行结果为 %s%n", this.getClass().getTypeName(), this.execute());
    }
}
