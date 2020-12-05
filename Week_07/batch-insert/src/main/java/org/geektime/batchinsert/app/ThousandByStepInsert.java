package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * 每次插入1000条数据
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("thousand-by-step-insert")
public class ThousandByStepInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return 1000;
    }
}
