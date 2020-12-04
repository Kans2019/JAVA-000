package org.geektime.support;

import com.zaxxer.hikari.HikariDataSource;
import org.geektime.common.DataSourceOperation;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/3
 * @since 1.8
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    public static ThreadLocal<DataSourceOperation> currentDataSourceId = ThreadLocal.withInitial(() -> DataSourceOperation.WRITE);

    @Override
    protected Object determineCurrentLookupKey() {
        return currentDataSourceId.get();
    }

    /**
     * 设置当前线程进行操作的数据库类型
     * @param key
     */
    public void setCurrentDataSourceId(DataSourceOperation key) {
        currentDataSourceId.set(key);
    }

    /**
     * 移除当前线程的数据库类型, 防止内存泄漏
     */
    public void removeCurrentDataSourceId() {
        currentDataSourceId.remove();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource dataSource = super.determineTargetDataSource();
        if (logger.isDebugEnabled()) {
            logger.debug("操作数据源 " + dataSource);
        }
        return dataSource;
    }
}
