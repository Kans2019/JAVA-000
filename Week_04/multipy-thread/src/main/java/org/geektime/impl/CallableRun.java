package org.geektime.impl;

import javafx.concurrent.Task;
import org.geektime.Homework;
import org.geektime.SynchronizedRun;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Terrdi
 * @description callable 封装 FutureTask 来获取结果
 * @date 2020/11/10
 * @see FutureTask
 */
public class CallableRun implements SynchronizedRun<Integer> {
    @Override
    public Integer execute() throws InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(Homework::sum);
        try {
            new Thread(task, "callable-run").start();
            return task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
