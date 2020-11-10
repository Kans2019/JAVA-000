package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Terrdi
 * @description CompletableFuture 来实现同步调用
 * @date 2020/11/10
 * @see CompletableFuture
 */
public class CompletableFutureRun1 implements SynchronizedRun<Integer> {
    @Override
    public Integer execute() throws InterruptedException {
        try {
            return CompletableFuture.supplyAsync(Homework::sum).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
