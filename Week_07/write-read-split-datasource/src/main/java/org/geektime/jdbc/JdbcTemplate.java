package org.geektime.jdbc;


import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 自定义读写分离的JdbcTemplate
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/1
 * @since 1.8
 * @see JdbcTemplate#getWriteConnection()
 * @see JdbcTemplate#getReadConnection()
 **/
@Component("geektimeJdbcTemplate")
public class JdbcTemplate implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    /**
     * 写连接池大小
     */
    private static final int WRITE_CONNECTION_LENGTH = 1 << 2;

    /**
     * 每个数据源读连接池大小
     */
    private static final int READ_CONNECTION_LENGTH_PER_DATASOURCE = 1 << 4;

    @Autowired
    @Qualifier("writeDataSource")
    private DataSource writeDataSource;

    @Autowired(required = false)
    @Qualifier("readDataSources")
    private List<DataSource> readDataSources;

    private ArrayBlockingQueue<Connection> writeBlockingQueue = new ArrayBlockingQueue<>(WRITE_CONNECTION_LENGTH);

    private ArrayBlockingQueue<Connection> readBlockingQueue;

    public int executeUpdate(String sql, Object... objects) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getWriteConnection();
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

    public int executeBatchUpdate(SqlStatement... sqlStatements) {
        int result = 0;
        if (Objects.isNull(sqlStatements) || sqlStatements.length == 0) {
            return result;
        }
        Statement statement = null;
        Connection connection = null;
        try {
            connection = this.getWriteConnection();
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

    /**
     * 查询方法
     * @param sql 查询的sql
     * @param objects 查询的参数
     * @return
     */
    public List<Map<String, Object>> query(String sql, Object... objects) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = this.getReadConnection();
            statement = this.preparedStatement(connection, sql, objects);
            rs = statement.executeQuery();
            List<Map<String, Object>> list = new LinkedList<>();
            List<String> columnNames = new ArrayList<>(rs.getMetaData().getColumnCount());
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columnNames.add(rs.getMetaData().getColumnLabel(i));
            }
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String column : columnNames) {
                    map.put(column, rs.getObject(column));
                }
                list.add(map);
            }
            return list;
        } catch (IOException | SQLException e) {
            logger.warn("获取连接失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                if (Objects.nonNull(statement)) statement.close();
            } catch (SQLException throwables) {
                logger.error("关闭连接错误", throwables);
            }
            this.releaseConnection(connection);
        }
    }

    public <T> List<T> query(Class<T> clazz, String sql, Object... objects) {
        List<Map<String, Object>> list = this.query(sql, objects);
        List<T> result = new ArrayList<>(list.size());
        list.forEach(map -> {
            T t = null;
            try {
                t = clazz.newInstance();
                BeanUtils.populate(t, map);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                if (Objects.nonNull(t)) {
                    result.add(t);
                }
            }
        });
        return result;
    }

    protected Connection getConnection() throws IOException {
        return this.getWriteConnection();
    }

    protected Connection getWriteConnection() throws IOException {
        Connection connection = this.writeBlockingQueue.poll();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("关闭自动提交失败", e);
            throw new IOException(e);
        }
        return connection;
    }

    protected Connection getReadConnection() throws IOException {
        try {
            return Objects.requireNonNull(this.readBlockingQueue, "没有读连接配置").poll(0, TimeUnit.NANOSECONDS);
        } catch (NullPointerException | InterruptedException e) {
            logger.warn("获取读连接失败 {}, 获取写连接...", e.getMessage());
            return this.getWriteConnection();
        }
    }

    protected void releaseConnection(Connection connection) {
        if (Objects.isNull(connection)) return;
        if (connection instanceof MainConnection) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("打开自动提交失败", e);
            }
            this.writeBlockingQueue.offer(connection);
        } else if (connection instanceof FollowConnection) {
            this.readBlockingQueue.offer(connection);
        } else {
            throw new UnsupportedOperationException("不支持的连接类型 " + connection.getClass().getTypeName());
        }

    }

    /**
     * 获取预处理声明
     * @param connection
     * @param sql
     * @param objects
     * @return
     * @throws SQLException
     */
    protected PreparedStatement preparedStatement(Connection connection, String sql, Object... objects) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            statement.setObject(i + 1, objects[i]);
        }
        return statement;
    }

    protected String parseSql(Connection connection, SqlStatement sqlStatement) throws SQLException {
        StringBuilder sql = new StringBuilder();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement.getSql())) {
            for (int i = 0;i < sqlStatement.getArguments().length;i++) {
                Object argu = sqlStatement.getArguments()[i];
                if (argu.getClass().isArray()) {
                    Object[] argums = (Object[]) argu;
                    for (int k = 0; k < argums.length;k++) {
                        preparedStatement.setObject(k + 1, argums[k]);
                    }
                    sql.append(this.parseSql(preparedStatement)).append(';');
                    preparedStatement.addBatch();
                } else if (argu instanceof Iterable) {
                    Iterator iterator = ((Iterable<?>) argu).iterator();
                    int k = 1;
                    while (iterator.hasNext()) {
                        preparedStatement.setObject(k++, iterator.next());
                    }
                    preparedStatement.addBatch();
                    sql.append(this.parseSql(preparedStatement)).append(';');
                } else {
                    preparedStatement.setObject(i + 1, argu);
                }
            }
            if (sql.length() == 0) {
                return this.parseSql(preparedStatement);
            } else {
                sql.deleteCharAt(sql.lastIndexOf(";"));
                return sql.toString();
            }
        }
    }

    /**
     * 从预处理的 {@link PreparedStatement} 中提取sql
     * @param preparedStatement 预处理的{@link PreparedStatement}
     * @return 编译后的sql
     */
    protected String parseSql(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement instanceof ClientPreparedStatement) {
            return ((ClientPreparedStatement) preparedStatement).asSql();
        } else {
            String tmpSql = preparedStatement.toString();
            return tmpSql.substring(tmpSql.lastIndexOf(':') + 1);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < WRITE_CONNECTION_LENGTH; i++) {
            this.writeBlockingQueue.offer(new MainConnection(this.writeDataSource.getConnection()));
        }

        if (!CollectionUtils.isEmpty(readDataSources)) {
            this.readBlockingQueue = new ArrayBlockingQueue<>(readDataSources.size() * READ_CONNECTION_LENGTH_PER_DATASOURCE);
            for (DataSource dataSource : this.readDataSources) {
                for (int i = 0; i < READ_CONNECTION_LENGTH_PER_DATASOURCE; i++) {
                    this.readBlockingQueue.offer(new FollowConnection(dataSource.getConnection()));
                }
            }
        }
    }

    /**
     * 主连接
     */
    final static class MainConnection extends Connection {
        public MainConnection(java.sql.Connection connection) {
            super(connection);
        }
    }

    /**
     * 从连接
     */
    final static class FollowConnection extends Connection {
        public FollowConnection(java.sql.Connection connection) {
            super(connection);
        }
    }
}
