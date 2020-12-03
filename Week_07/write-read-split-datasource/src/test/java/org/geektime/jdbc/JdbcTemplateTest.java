package org.geektime.jdbc;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ActiveProfiles("jdbc")
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @org.junit.Test
    public void testSelect() {
        String sql = "select id,name from t_java_course where id=?";
        System.out.println(jdbcTemplate.query(sql, 1));
        System.out.println(jdbcTemplate.query(sql, 2));
        System.out.println(jdbcTemplate.query(sql, 3));
    }
}