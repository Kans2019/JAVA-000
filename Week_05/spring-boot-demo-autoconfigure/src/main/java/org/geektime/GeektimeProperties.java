package org.geektime;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuhanwei
 * @description
 * @date 2020/11/17
 */
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
