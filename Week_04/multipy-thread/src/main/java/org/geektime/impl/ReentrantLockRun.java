package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuhanwei
 * @description 使用可重入锁
 * @date 2020/11/10
 * @see ReentrantLock
 * @see Condition
 */
public class ReentrantLockRun implements SynchronizedRun<Integer> {
    private final Lock lock = new ReentrantLock();

    private final AtomicInteger value = new AtomicInteger();

    private final Condition condition = lock.newCondition();

    @Override
    public Integer execute() throws InterruptedException {
        new Thread(() -> {
            try {
                lock.tryLock();
                value.set(Homework.sum());
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "reentrant-lock-run").start();
        try {
            lock.tryLock();
            condition.await();
            return value.get();
        } finally {
            lock.unlock();
        }
    }
}
