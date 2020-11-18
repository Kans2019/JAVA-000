package org.geektime.enhance;

import org.apache.log4j.Logger;
import org.geektime.JdbcTemplate;
import org.geektime.support.Connection;
import org.geektime.support.DataBase;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 原生JDBC(包含事务机制)的模版
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public class JdbcTransactionTemplate extends JdbcTemplate {
    private static final Logger logger = Logger.getLogger(JdbcTemplate.class);

    public JdbcTransactionTemplate(java.sql.Connection connection) {
        super(connection);
    }

    public JdbcTransactionTemplate(DataBase dataBase, String host, int port, String database, String userName, String password) throws ReflectiveOperationException, SQLException {
        super(dataBase, host, port, database, userName, password);
    }

    public JdbcTransactionTemplate(DataBase dataBase, String host, String database, String userName, String password) throws SQLException, ReflectiveOperationException {
        super(dataBase, host, database, userName, password);
    }

    @Override
    public int executeUpdate(String sql, Object... objects) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getConnection();
            statement = this.preparedStatement(connection, sql, objects);
            return statement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.warn("获取连接失败", e);
        } finally {
            try {
                if (Objects.nonNull(statement)) statement.close();
            } catch (SQLException throwables) {
                logger.error("关闭连接错误", throwables);
            }
            this.releaseConnection(connection);
        }
        return 0;
    }

    @Override
    protected Connection getConnection() throws IOException {
        Connection connection = super.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("关闭自动提交失败", e);
            throw new IOException(e);
        }
        return connection;
    }

    @Override
    protected void releaseConnection(Connection connection) {
        super.releaseConnection(connection);
    }
}
