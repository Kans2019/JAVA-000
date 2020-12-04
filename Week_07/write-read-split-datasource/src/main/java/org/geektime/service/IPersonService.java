package org.geektime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.geektime.pojo.Person;

import java.util.List;

/**
 * SpringAop 包装的目标类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
public interface IPersonService extends IService<Person> {
    /**
     * 根据id 获取 {@link Person}
     * @param id
     * @return
     */
    Person getById(Long id);

    /**
     * 获取所有 {@link Person}
     * @return
     */
    List<Person> listAll();
}
