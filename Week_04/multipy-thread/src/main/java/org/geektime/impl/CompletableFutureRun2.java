package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Terrdi
 * @description CompletableFuture 来实现同步调用
 * @date 2020/11/10
 * @see CompletableFuture
 */
public class CompletableFutureRun2 implements SynchronizedRun<Integer> {
    private final AtomicInteger value = new AtomicInteger();

    @Override
    public Integer execute() throws InterruptedException {
        try {
            CompletableFuture.runAsync(() -> {
                this.value.set(Homework.sum());
            }).get();
            return this.value.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
