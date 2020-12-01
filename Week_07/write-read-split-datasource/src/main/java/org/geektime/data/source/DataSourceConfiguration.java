package org.geektime.data.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    @ConditionalOnProperty(prefix = "geektime", name = "read")
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
    public List<DataSource> readDataSources(@Qualifier("readDataSourceProperties") List<DataSourceProperties> read) {
        return read.stream().parallel().map(DataSourceProperties::initializeDataSourceBuilder)
                .map(DataSourceBuilder::build).collect(Collectors.toList());
    }
}
