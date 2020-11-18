package org.geektime;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 注入的参数类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
@ConfigurationProperties(prefix = "geektime")
public class GeektimeProperties {
    private Student student;

    private Klass klass;

    private School school;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Klass getKlass() {
        return klass;
    }

    public void setKlass(Klass klass) {
        this.klass = klass;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
