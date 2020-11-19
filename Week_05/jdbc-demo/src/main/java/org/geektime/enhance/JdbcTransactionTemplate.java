package org.geektime.enhance;

import org.apache.log4j.Logger;
import org.geektime.JdbcTemplate;
import org.geektime.support.Connection;
import org.geektime.support.DataBase;
import org.geektime.support.SqlStatement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;

/**
 * 原生JDBC(包含事务机制)的模版
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public class JdbcTransactionTemplate extends JdbcTemplate {
    private static final Logger logger = Logger.getLogger(JdbcTransactionTemplate.class);

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
            int result = statement.executeUpdate();
            connection.commit();
            return result;
        } catch (IOException | SQLException e) {
            if (Objects.nonNull(connection)) {
                try {
                    connection.rollback();
                } catch (SQLException throwables) {
                    logger.error("回滚失败", throwables);
                }
            }
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
    public int executeBatchUpdate(SqlStatement... sqlStatements) {
        int result = 0;
        if (Objects.isNull(sqlStatements) || sqlStatements.length == 0) {
            return result;
        }
        Statement statement = null;
        Connection connection = null;
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            for (SqlStatement sqlStatement : sqlStatements) {
                String sql = this.parseSql(connection, sqlStatement);
                for (String singleSql : sql.split(";")) {
                    statement.addBatch(singleSql);
                }
            }
            result = Arrays.stream(statement.executeBatch()).sum();
            connection.commit();
            return result;
        } catch (IOException | SQLException e) {
            if (Objects.nonNull(statement)) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    logger.error("关闭sql语句失败", throwables);
                }
            }
            if (Objects.nonNull(connection)) {
                try {
                    connection.rollback();
                } catch (SQLException throwables) {
                    logger.error("回滚失败", throwables);
                }
            }
            logger.error("批量更新失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.releaseConnection(connection);
        }
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
        if (Objects.isNull(connection)) return;
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("打开自动提交失败", e);
        }
        super.releaseConnection(connection);
    }
}
