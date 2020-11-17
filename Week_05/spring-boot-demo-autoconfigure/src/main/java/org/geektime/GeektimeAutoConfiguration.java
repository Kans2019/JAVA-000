package org.geektime;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.geektime.support.IgnoreBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author liuhanwei
 * @description
 * @date 2020/11/17
 */
@Configuration
@EnableConfigurationProperties(GeektimeProperties.class)
public class GeektimeAutoConfiguration {
    private final GeektimeProperties geektimeProperties;

    private final School school;

    public GeektimeAutoConfiguration(GeektimeProperties geektimeProperties) {
        this.geektimeProperties = geektimeProperties;
        this.school = this.geektimeProperties.getSchool();
    }

    @Bean
    public Student student100() {
        return geektimeProperties.getStudent();
    }

    @IgnoreBean(ignoreAutowiredPostProcessor = true)
    public School school() {
        return school;
    }

    @Bean
    public Klass klass() {
        return geektimeProperties.getKlass();
    }
}
