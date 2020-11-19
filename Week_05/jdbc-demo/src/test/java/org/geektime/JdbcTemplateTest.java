package org.geektime;

import com.google.common.collect.Lists;
import org.geektime.enhance.HikariJdbcTemplate;
import org.geektime.enhance.JdbcTransactionTemplate;
import org.geektime.pojo.Student;
import org.geektime.support.DataBase;
import org.geektime.support.SqlStatement;
import org.junit.After;
import org.junit.Assert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class JdbcTemplateTest {
    private Properties properties = new Properties();

    private final String jdbcFile = "jdbc.properties";

    private JdbcTemplate jdbcTemplate;

    @org.junit.Before
    public void loadJdbcProperties() throws IOException, SQLException, ReflectiveOperationException {
        properties.load(JdbcTemplateTest.class.getResourceAsStream("/" + jdbcFile));
        jdbcTemplate = new JdbcTransactionTemplate(DataBase.MYSQL_8,
                properties.getProperty("jdbc.host"),
                Integer.parseInt(properties.getProperty("jdbc.port")),
                properties.getProperty("jdbc.databasename"),
                properties.getProperty("jdbc.username"),
                properties.getProperty("jdbc.password"));

        this.jdbcTemplate.executeUpdate("delete from t_java_course");
        this.jdbcTemplate.executeUpdate("ALTER TABLE t_java_course AUTO_INCREMENT=?", 2);
    }

    @After
    public void close() throws IOException {
        this.jdbcTemplate.close();
    }

    @org.junit.Test
    public void testInsert() {
        StringBuilder sql = new StringBuilder("insert into t_java_course(name) values (?)");
        List<String> names = Lists.newArrayList("鼠", "牛", "虎", "兔",
                                                          "龙", "蛇", "马", "羊",
                                                          "猴", "鸡", "狗", "猪");
        Assert.assertEquals(names.size(), jdbcTemplate.executeBatchUpdate(new SqlStatement(sql.toString(),
                names.stream().map(Collections::singletonList).map(List::toArray).toArray())));
    }

    @org.junit.Test
    public void testSelect() {
        String sql = "select id,name from t_java_course where id=?";
        System.out.println(jdbcTemplate.query(sql, 1));
    }

    @org.junit.Test
    public void testSelectClass() {
        String sql = "select id,name from t_java_course where id>?";
        System.out.println(jdbcTemplate.query(Student.class, sql, 0));
    }

    @org.junit.Test
    public void testBatchUpdate() {
        Assert.assertTrue(0 < jdbcTemplate.executeBatchUpdate(
                new SqlStatement("update t_java_course set name=? where id >= 200", "火离"),
                new SqlStatement("insert into t_java_course(id, name) values(?, ?)", 2, "风巽")
        ));
        Assert.assertEquals(1, jdbcTemplate.executeBatchUpdate(
                new SqlStatement("update t_java_course set name=? where id >= 12", "泽兑"),
                new SqlStatement("insert into t_java_course(id, name) values(?, ?)", 1, "水坎")
        ));
    }
}