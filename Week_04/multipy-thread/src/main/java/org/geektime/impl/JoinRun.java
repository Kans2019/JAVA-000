package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/10
 * @see Thread#join()
 */
public class JoinRun implements SynchronizedRun<Integer> {
    /**
     * 同步结果
     */
    private final AtomicInteger value = new AtomicInteger();

    /**
     * 加上同步 方式 value 被修改
     * @return
     */
    public synchronized Integer execute() throws InterruptedException {
        Thread thread = new Thread(() -> {
            int v = Homework.sum();
            value.set(v);
        }, "joinRun");
        thread.start();
        thread.join();
        return value.get();
    }
}
