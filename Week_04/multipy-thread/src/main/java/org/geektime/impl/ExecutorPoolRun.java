package org.geektime.impl;

import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.*;

/**
 * @author Terrdi
 * @description 线程池执行
 * @date 2020/11/10
 * @see Future
 */
public class ExecutorPoolRun implements SynchronizedRun<Integer> {
    private final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() << 1,
            1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(20),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public Integer execute() throws InterruptedException {
        Future<Integer> future = executorService.submit(Homework::sum);
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }

    }
}
