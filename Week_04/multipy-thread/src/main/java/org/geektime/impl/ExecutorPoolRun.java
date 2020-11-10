package org.geektime.impl;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
import org.geektime.Homework;
import org.geektime.SynchronizedRun;
import sun.tools.java.RuntimeConstants;

import java.util.concurrent.*;

/**
 * @author liuhanwei
 * @description 线程池执行
 * @date 2020/11/10
 */
public class ExecutorPoolRun implements SynchronizedRun<Integer> {
    private ExecutorService executorService = new ThreadPoolExecutor(
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
