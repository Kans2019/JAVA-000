package org.geektime.service.impl;

import org.geektime.JdbcTemplate;
import org.geektime.pojo.Student;
import org.geektime.service.IStudentService;
import org.geektime.support.cache.annotation.MyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
@Service
public class StudentService implements IStudentService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @MyCache
    public Student getById(Integer id) {
        List<Student> list = jdbcTemplate.query(Student.class, "select id,name from t_java_course where id=?", id);
        return list.isEmpty() ? null : list.get(0);
    }
}
