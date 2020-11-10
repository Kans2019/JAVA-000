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
public class CompletableFutureRun3 implements SynchronizedRun<Integer> {
    @Override
    public Integer execute() throws InterruptedException {
        return CompletableFuture.supplyAsync(Homework::sum).join();
    }
}
