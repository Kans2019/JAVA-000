package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("ten-by-step-insert")
public class TenByStepInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return 10;
    }
}
