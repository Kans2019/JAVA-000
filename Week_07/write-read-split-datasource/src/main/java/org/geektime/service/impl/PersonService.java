package org.geektime.service.impl;

import org.geektime.jdbc.JdbcTemplate;
import org.geektime.pojo.Person;
import org.geektime.support.ReadOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * {@link org.geektime.service.IPersonService} 的实现类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 * @see org.geektime.service.IPersonService
 **/
@Service
public class PersonService implements org.geektime.service.IPersonService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @ReadOnly
    public Person getById(Long id) {
        return jdbcTemplate.query(Person.class, "select id,name from t_java_course where id = ?", id)
                .stream().findFirst().orElseThrow(() -> new IllegalArgumentException(id + " 不存在"));
    }

    @Override
    @ReadOnly
    public List<Person> listAll() {
        return jdbcTemplate.query(Person.class, "select id,name from t_java_course");
    }

    @Override
    public void save(Person person) {
        if (Objects.isNull(person) || Objects.isNull(person.getName()) || "".equals(person.getName().trim())) {
            throw new IllegalArgumentException("需要用户名");
        }
        if (Objects.isNull(person.getId())) {
            assert 1 == jdbcTemplate.executeUpdate("insert into t_java_course(name) values(?)", person.getName());
        } else {
            assert 1 == jdbcTemplate.executeUpdate("update t_java_course set name = ? where id = ?", person.getName(), person.getId());
        }

    }
}
