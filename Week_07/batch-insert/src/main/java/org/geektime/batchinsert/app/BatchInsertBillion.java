package org.geektime.batchinsert.app;

import org.geektime.batchinsert.pojo.Order;
import org.geektime.batchinsert.support.IdGenerator;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 测试插入100W数据量
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/5
 * @since 1.8
 **/
public abstract class BatchInsertBillion implements ApplicationRunner, BeanNameAware {
    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected List<Order> list;

    protected IdGenerator<Integer> idGenerator;

    private String beanName;

    private final String sql = "insert into geektime_order (order_no, user_id,shipping_id, payment, payment_type,postage, status, payment_time, send_time, end_time, close_time, create_time, update_time) values (?, ?,?, ?, ?, ?, ?,?,?, ?, ?,now(), now())";

    @Override
    public void setBeanName(String s) {
        this.beanName = s;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            long start = System.currentTimeMillis();
            this.batchInsert(list);
            System.out.printf("%s[%s] 运行时间 %ds.%n", this.beanName, Thread.currentThread().getName(),
                    Duration.ofMillis(System.currentTimeMillis() - start).getSeconds());
        } finally {
            jdbcTemplate.update("truncate table geektime_order");
        }
    }

    protected abstract int getBatchSize();

    protected void batchInsert(List<Order> list) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Iterator<Order> iterator = list.iterator();
        final int batchSize = this.getBatchSize();
        assert batchSize > 0;
        try {
            connection = dataSource.getConnection();
            while (iterator.hasNext()) {
                preparedStatement = connection.prepareStatement(sql);
                for (int i = 0; i < batchSize && iterator.hasNext(); i++) {
                    Order order = iterator.next();
                    preparedStatement.setLong(1, order.getOrderNo());
                    preparedStatement.setInt(2, order.getUserId());
                    preparedStatement.setInt(3, order.getShippingId());
                    preparedStatement.setBigDecimal(4, order.getPayment());
                    preparedStatement.setInt(5, order.getPaymentType());
                    preparedStatement.setInt(6, order.getPostage());
                    preparedStatement.setInt(7, order.getStatus());
                    preparedStatement.setDate(8, new java.sql.Date(order.getPaymentTime().getTime()));
                    preparedStatement.setDate(9, new java.sql.Date(order.getSendTime().getTime()));
                    preparedStatement.setDate(10, new java.sql.Date(order.getEndTime().getTime()));
                    preparedStatement.setDate(11, new java.sql.Date(order.getCloseTime().getTime()));

                    preparedStatement.addBatch();
                }
                preparedStatement.executeLargeUpdate();
                preparedStatement.clearBatch();
                preparedStatement.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                preparedStatement.close();
            }
            if (Objects.nonNull(connection)) {
                connection.close();
            }
        }
    }
}
