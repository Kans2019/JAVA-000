package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Terrdi
 * @description LockSupport 同步线程
 * @date 2020/11/10
 * @see java.util.concurrent.locks.LockSupport
 */
public class LockSupportRun implements SynchronizedRun<Integer> {
    /**
     * 同步结果
     */
    private final AtomicInteger value = new AtomicInteger();

    /**
     * 主线程
     */
    private final Thread main = Thread.currentThread();

    @Override
    public Integer execute() throws InterruptedException {
        Thread thread = new Thread(()->{
            value.set(Homework.sum());
            LockSupport.unpark(main);
        }, "lock-support-run");
        thread.start();
        LockSupport.park();
        return value.get();
    }
}
