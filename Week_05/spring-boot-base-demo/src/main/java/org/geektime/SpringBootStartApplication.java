package org.geektime;

import io.kimmking.spring02.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot启动类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
@SpringBootApplication
public class SpringBootStartApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStartApplication.class, args);
    }

    @Autowired
    private School school;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(school);
    }
}
