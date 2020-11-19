package org.geektime.enhance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;
import org.geektime.JdbcTemplate;
import org.geektime.support.Connection;
import org.geektime.support.DataBase;
import org.geektime.support.SqlStatement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
public class HikariJdbcTemplate extends JdbcTransactionTemplate {
    private static final Logger logger = Logger.getLogger(HikariJdbcTemplate.class);

    private final HikariDataSource dataSource;

    public HikariJdbcTemplate(DataBase dataBase, String host, int port, String database, String userName, String password) throws ReflectiveOperationException, SQLException {
        super(null);
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setConnectionTimeout(1000);
        config.setDriverClassName(dataBase.getDriverClass());
        config.setUsername(userName);
        config.setPassword(password);
        config.setJdbcUrl(String.format("%s%s:%d/%s?allowMultiQueries=true&allowPublicKeyRetrieval=true", dataBase.getUrlPrefix(), host, port, database));

        dataSource = new HikariDataSource(config);
    }

    public HikariJdbcTemplate(DataBase dataBase, String host, String database, String userName, String password) throws SQLException, ReflectiveOperationException {
        this(dataBase, host, dataBase.getDefaultPort(), database, userName, password);
    }

    @Override
    protected Connection getConnection() throws IOException {
        try {
            return new Connection(dataSource.getConnection());
        } catch (SQLException throwables) {
            throw new IOException(throwables);
        }
    }

    @Override
    protected void releaseConnection(Connection connection) {
        if (Objects.nonNull(connection)) {
            try {
                connection.commit();
            } catch (SQLException throwables) {
                logger.warn("提交事务失败", throwables);
            }
            dataSource.evictConnection(connection);
        }

    }

//    @Override
//    protected String parseSql(Connection connection, SqlStatement sqlStatement) throws SQLException {
//        String tmpSql = super.parseSql(connection, sqlStatement);
//        return tmpSql.substring(tmpSql.lastIndexOf(':') + 1);
//    }

    @Override
    protected String parseSql(PreparedStatement preparedStatement) throws SQLException {
        String tmpSql =  super.parseSql(preparedStatement);
        return tmpSql.substring(tmpSql.lastIndexOf(':') + 1);
    }
}
