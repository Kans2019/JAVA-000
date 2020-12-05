package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * 每次插入100条数据
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("hundred-by-step-insert")
public class HundredByStepInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return 100;
    }
}
