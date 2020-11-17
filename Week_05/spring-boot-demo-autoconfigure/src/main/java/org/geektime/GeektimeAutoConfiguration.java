package org.geektime;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuhanwei
 * @description
 * @date 2020/11/17
 */
@Configuration
@EnableConfigurationProperties(GeektimeProperties.class)
public class GeektimeAutoConfiguration {
    private final GeektimeProperties geektimeProperties;

    public GeektimeAutoConfiguration(GeektimeProperties geektimeProperties) {
        this.geektimeProperties = geektimeProperties;
    }

    @Bean
    public Student student100() {
        return geektimeProperties.getStudent();
    }

    @Bean
    public School school() {
        return geektimeProperties.getSchool();
    }

    @Bean
    public Klass klass() {
        return geektimeProperties.getKlass();
    }
}
