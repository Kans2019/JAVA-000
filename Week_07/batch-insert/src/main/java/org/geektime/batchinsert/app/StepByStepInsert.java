package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * 一个一个插入数据
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("step-by-step-insert")
public class StepByStepInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return 1;
    }
}
