package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description 使用CyclicBarrier来计算
 * @date 2020/11/10
 * @see CyclicBarrier
 */
public class CyclicBarrierRun extends Thread implements SynchronizedRun<Integer> {
    private final CyclicBarrier cyclicBarrier;

    final AtomicInteger value = new AtomicInteger();

    public CyclicBarrierRun(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        this.setName("cyclic-barrier-run");
    }

    public CyclicBarrierRun() {
        this(new CyclicBarrier(2));
    }

    @Override
    public Integer execute() throws InterruptedException {
        this.start();
        try {
            // 等待计算完成
            this.cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return this.value.get();
    }

    @Override
    public void run() {
        this.value.set(Homework.sum());
        try {
            this.cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
