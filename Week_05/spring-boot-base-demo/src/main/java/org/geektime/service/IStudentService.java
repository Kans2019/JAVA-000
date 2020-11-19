package org.geektime.service;

import org.geektime.pojo.Student;

/**
 * 学生服务类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/19
 * @since 1.8
 **/
public interface IStudentService {
    /**
     * 根据id获取学生信息
     * @param id
     * @return
     */
    Student getById(Integer id);
}
