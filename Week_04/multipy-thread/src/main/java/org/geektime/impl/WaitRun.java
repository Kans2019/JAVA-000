package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description wait 来完成同步获取结果
 * @date 2020/11/10
 * @see Object#wait()
 * @see Object#notify()
 */
public class WaitRun implements SynchronizedRun<Integer> {
    private final AtomicInteger value = new AtomicInteger();

    @Override
    public Integer execute() throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                value.set(Homework.sum());
                notify();
            }
        }, "wait-run");
        thread.start();
        synchronized (this) {
            wait();
        }
        return value.get();
    }
}
