package org.geektime;


import org.apache.log4j.Logger;
import org.geektime.impl.*;

/**
 * @author Terrdi
 * @description 家庭作业
 * @date 2020/11/10
 */
public class Homework {
    private static Logger logger = Logger.getLogger(Homework.class);

    public static int sum() {
        if (logger.isInfoEnabled()) {
            logger.info("执行 sum 函数");
        }
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        int a1 = 0, a2 = 1;
        for (int i = 2;i <= a;i++) {
            a2 = a1 + (a1 = a2);
        }
        return a1 + a2;
    }

    public static void main(String[] args) {
        try {
            new JoinRun().print();
            new CallableRun().print();
            new ExecutorPoolRun().print();
            new WaitRun().print();
            new SemaphoreRun().print();
            new CyclicBarrierRun().print();
            new CountDownLatchRun().print();
            new ReentrantLockRun().print();
            new CompletableFutureRun1().print();
            new CompletableFutureRun2().print();
            new CompletableFutureRun3().print();
            new CompletableFutureRun4().print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
