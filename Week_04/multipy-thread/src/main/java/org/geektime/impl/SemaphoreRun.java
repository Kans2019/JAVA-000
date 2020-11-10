package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuhanwei
 * @description 使用信号量来完成同步
 * @date 2020/11/10
 * @see Semaphore
 */
public class SemaphoreRun extends Thread implements SynchronizedRun<Integer> {
    private final AtomicInteger value = new AtomicInteger();

    private final Semaphore semaphore;

    public SemaphoreRun(Semaphore semaphore) {
        this.semaphore = semaphore;
        this.setName("semaphore-run");
    }

    public SemaphoreRun() {
        this(new Semaphore(0));
    }

    @Override
    public Integer execute() throws InterruptedException {
        this.start();
        semaphore.acquire();
        return value.get();
    }

    @Override
    public void run() {
        try {
            value.set(Homework.sum());
        } finally {
            semaphore.release();
        }
    }
}
