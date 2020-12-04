package org.geektime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.geektime.pojo.Person;

/**
 * {@link Person} 的 Mapper
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
