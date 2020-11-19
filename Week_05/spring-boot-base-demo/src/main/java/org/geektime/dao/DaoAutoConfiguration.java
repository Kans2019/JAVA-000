package org.geektime.dao;

import org.geektime.JdbcTemplate;
import org.geektime.enhance.JdbcTransactionTemplate;
import org.geektime.support.cache.DataBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 自实现的JdbcTemplate
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
@Configuration
public class DaoAutoConfiguration {
    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException, ReflectiveOperationException, IOException {
        Properties properties = new Properties();
        properties.load(DaoAutoConfiguration.class.getResourceAsStream("/jdbc.properties"));
        return new JdbcTransactionTemplate(DataBase.MYSQL_8,
                properties.getProperty("jdbc.host"),
                Integer.parseInt(properties.getProperty("jdbc.port")),
                properties.getProperty("jdbc.databasename"),
                properties.getProperty("jdbc.username"),
                properties.getProperty("jdbc.password"));
    }


}
