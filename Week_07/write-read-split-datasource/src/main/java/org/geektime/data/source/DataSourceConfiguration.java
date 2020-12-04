package org.geektime.data.source;

import org.geektime.common.DataSourceOperation;
import org.geektime.support.ConditionalOnPropertyExists;
import org.geektime.support.DynamicDataSource;
import org.geektime.support.StrategyDataSource;
import org.geektime.support.WeightDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 读取数据源配置
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/1
 * @since 1.8
 **/
@Profile("jdbc")
@Configuration
public class DataSourceConfiguration {
    @Autowired
    private Environment env;

    @Primary
    @Bean("writeDataSourceProperties")
    @ConfigurationProperties("geektime.write")
    public DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("readDataSourceProperties")
    @ConfigurationProperties("geektime.read")
    @ConditionalOnPropertyExists(name = "geektime.read")
    public List<DataSourceProperties> readDataSourceProperties() {
        return new ArrayList<>();
    }

    @Bean("writeDataSource")
    @Primary
    public DataSource writeDataSource(@Qualifier("writeDataSourceProperties") DataSourceProperties write) {
        return write.initializeDataSourceBuilder().build();
    }

    @Bean("readDataSources")
    @ConditionalOnBean(name = "readDataSourceProperties")
    @ConditionalOnProperty(prefix = "geektime", name = "strategy", havingValue = "weight")
    public DataSource readWeightDataSources2(@Qualifier("readDataSourceProperties") List<DataSourceProperties> read) {
        List<DataSource> list = resolveDataSources(read);
        for (int i = 0; i < list.size(); i++) {
            final int index = i;
            list.set(index, new WeightDataSource(list.get(index)) {
                @Override
                public int getWeight() {
                    return env.getProperty("geektime.read[" + index + "].weight", Integer.class, 0);
                }
            });
        }
        return new StrategyDataSource(list);
    }

    @Bean("readDataSources")
    @ConditionalOnBean(name = "readDataSourceProperties")
    @ConditionalOnExpression("!'${geektime.strategy}'.equals('weight')")
    public DataSource readDataSources1(@Qualifier("readDataSourceProperties") List<DataSourceProperties> read) {
        return new StrategyDataSource(resolveDataSources(read));
    }

    @Bean("dynamicDataSource")
    @ConditionalOnMissingBean(name = "readDataSources")
    public DynamicDataSource dynamicDataSource2(@Qualifier("writeDataSource") DataSource writeDataSource) throws SQLException {
        DynamicDataSource data = new DynamicDataSource();
        Map<Object, Object> map = new HashMap<>();
        map.put(DataSourceOperation.WRITE, writeDataSource);
        data.setTargetDataSources(map);
        data.setDefaultTargetDataSource(writeDataSource);
        return data;
    }

    @Bean("dynamicDataSource")
    @ConditionalOnBean(name = "readDataSources")
    public DynamicDataSource dynamicDataSource1(@Qualifier("writeDataSource") DataSource writeDataSource, @Qualifier("readDataSources") DataSource readDataSources) throws SQLException {
        DynamicDataSource data = new DynamicDataSource();
        Map<Object, Object> map = new HashMap<>();
        map.put(DataSourceOperation.WRITE, writeDataSource);
        map.put(DataSourceOperation.READ, readDataSources);
        data.setTargetDataSources(map);
        data.setDefaultTargetDataSource(writeDataSource);
        return data;
    }

    private List<DataSource> resolveDataSources(Collection<DataSourceProperties> read) {
        return read.stream().parallel().map(DataSourceProperties::initializeDataSourceBuilder)
                .map(DataSourceBuilder::build).collect(Collectors.toList());
    }
}
