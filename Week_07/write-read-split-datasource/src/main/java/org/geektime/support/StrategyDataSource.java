package org.geektime.support;

import org.geektime.support.strategy.ProxyStrategy;
import org.geektime.support.wrapper.DataSourceWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

/**
 * 支持负载均衡策略的数据源
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
public class StrategyDataSource extends DataSourceWrapper {
    private final List<? extends DataSource> dataSources;

    public StrategyDataSource(List<? extends DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    @Autowired(required = false)
    private ProxyStrategy<? extends DataSource> proxyStrategy;

    @SuppressWarnings("all")
    public DataSource getDataSource() {
        return Objects.requireNonNull(this.proxyStrategy).getNext((List) this.dataSources);
    }
}
