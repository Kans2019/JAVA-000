package org.geektime;

import io.kimmking.spring02.School;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;

/**
 * SpringBoot启动类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
@SpringBootApplication
public class SpringBootStartApplication implements CommandLineRunner, ApplicationContextAware {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStartApplication.class, args);
    }

    @Autowired
    @Qualifier("school")
    private School school;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(school);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    }
}
