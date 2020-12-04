package org.geektime.support;

import org.geektime.support.strategy.WeightFul;
import org.geektime.support.wrapper.DataSourceWrapper;

import javax.sql.DataSource;

/**
 * 可以查询权重的代理对象
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/3
 * @since 1.8
 **/
public abstract class WeightDataSource extends DataSourceWrapper implements WeightFul {
    /**
     * 包装的对象类
     */
    protected final DataSource dataSource;

    public WeightDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
