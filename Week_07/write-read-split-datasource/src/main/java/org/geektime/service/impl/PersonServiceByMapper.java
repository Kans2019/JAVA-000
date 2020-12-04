package org.geektime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.geektime.mapper.PersonMapper;
import org.geektime.pojo.Person;
import org.geektime.service.IPersonService;
import org.geektime.support.ReadOnly;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
@Service
public class PersonServiceByMapper extends ServiceImpl<PersonMapper, Person> implements IPersonService  {
    @Override
    @ReadOnly
    public Person getById(Long id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @ReadOnly
    public List<Person> listAll() {
        return getBaseMapper().selectList(new QueryWrapper<>());
    }

    @Override
    public boolean save(Person entity) {
        return super.saveOrUpdate(entity);
    }
}
