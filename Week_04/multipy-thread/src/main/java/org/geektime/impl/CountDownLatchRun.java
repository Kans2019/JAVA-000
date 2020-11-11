package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description CountDownLatch 实现同步
 * @date 2020/11/10
 * @see CountDownLatch
 */
public class CountDownLatchRun extends Thread implements SynchronizedRun<Integer> {
    private final CountDownLatch countDownLatch;

    private final AtomicInteger value = new AtomicInteger();

    public CountDownLatchRun(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.setName("count-down-latch-run");
    }

    public CountDownLatchRun() {
        this(new CountDownLatch(1));
    }

    @Override
    public Integer execute() throws InterruptedException {
        this.start();
        countDownLatch.await();
        return value.get();
    }

    @Override
    public void run() {
        this.value.set(Homework.sum());
        this.countDownLatch.countDown();
    }
}
