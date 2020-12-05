package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * 每次插入一万条数据
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("ten-thousand-by-step-insert")
public class TenThousandByStepInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return 10000;
    }
}
