package org.geektime.data.source;

import org.geektime.support.ConditionalOnPropertyExists;
import org.geektime.support.strategy.WeightDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
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
    @ConditionalOnExpression("'${geektime.read.strategy}'.equals('weight')")
    public List<DataSource> readWeightDataSources(@Qualifier("readDataSourceProperties") List<DataSourceProperties> read) {
        List<DataSource> list = readDataSources(read);
        for (int i = 0; i < list.size(); i++) {
            final int index = i;
            list.set(index, new WeightDataSource(list.get(index)) {
                @Override
                public int getWeight() {
                    return env.getProperty("geektime.read[" + index + "].weight", Integer.class, 0);
                }
            });
        }
        return list;
    }

    @Bean("readDataSources")
    @ConditionalOnBean(name = "readDataSourceProperties")
    @ConditionalOnExpression("not '${geektime.read.strategy}'.equals('weight')")
    public List<DataSource> readDataSources(@Qualifier("readDataSourceProperties") List<DataSourceProperties> read) {
        return read.stream().parallel().map(DataSourceProperties::initializeDataSourceBuilder)
                .map(DataSourceBuilder::build).collect(Collectors.toList());
    }
}
