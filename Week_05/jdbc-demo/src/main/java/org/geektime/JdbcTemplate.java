package org.geektime;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.geektime.support.Connection;
import org.geektime.support.DataBase;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 原生JDBC的模版
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public class JdbcTemplate implements Closeable {
    private static final Logger logger = Logger.getLogger(JdbcTemplate.class);

    /**
     * 模拟一个连接池
     */
    private final ArrayBlockingQueue<Connection> connectionPool = new ArrayBlockingQueue<>(16);

    private AtomicBoolean isClosed = new AtomicBoolean(false);

    public JdbcTemplate(java.sql.Connection connection) {
        this.connectionPool.add(new Connection(connection));
    }

    public JdbcTemplate(DataBase dataBase, String host, int port, String database,
                      String userName, String password) throws ReflectiveOperationException, SQLException {
        for (int i = 0; i < connectionPool.remainingCapacity(); i++) {
            this.connectionPool.add(new Connection(dataBase, host, port, database, userName, password));
        }
    }

    public JdbcTemplate(DataBase dataBase, String host, String database, String userName, String password) throws SQLException, ReflectiveOperationException {
        this(dataBase, host, dataBase.getDefaultPort(), database, userName, password);
    }

    /**
     * 运行更新数据的sql数据
     * @param sql sql语句
     * @param objects sql语句的参数
     * @return 影响的行数
     */
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
            connection = this.getConnection();
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
        } finally {
            try {
                if (Objects.nonNull(statement)) statement.close();
            } catch (SQLException throwables) {
                logger.error("关闭连接错误", throwables);
            }
            this.releaseConnection(connection);
        }
        return Collections.emptyList();
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


    /**
     * 从连接池中获取连接
     * @return
     * @throws IOException
     */
    protected Connection getConnection() throws IOException {
        if (this.isClosed.get()) {
            throw new IOException("连接池已关闭");
        }
        return this.connectionPool.poll();
    }

    /**
     * 归还连接
     */
    protected void releaseConnection(Connection connection) {
        this.connectionPool.offer(connection);
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

    @Override
    public void close() throws IOException {
        if (!this.isClosed.getAndSet(true)) {
            try {
                while (!this.connectionPool.isEmpty()) {
                    Connection connection = this.connectionPool.remove();
                    connection.close();
                }
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }
}
