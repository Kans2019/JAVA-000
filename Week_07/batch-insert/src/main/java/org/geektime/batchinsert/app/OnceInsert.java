package org.geektime.batchinsert.app;

import org.springframework.stereotype.Component;

/**
 * 一次性插入所有数据
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
@Component("once-insert")
public class OnceInsert extends BatchInsertBillion {
    @Override
    protected int getBatchSize() {
        return list.size();
    }
}
