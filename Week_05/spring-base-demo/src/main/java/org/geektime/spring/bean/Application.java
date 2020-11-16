package org.geektime.spring.bean;

import io.kimmking.spring01.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring 启动类
 * 配置类 使用{@link Bean}装配
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 * @see Configuration
 * @see Bean
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
 **/
@Configuration
public class Application {
    /**
     * Spring 配置文件的地址
     */
    private static final String APPLICATION_XML = "classpath:application.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{APPLICATION_XML}, false
                , ExtBeanDefinitionApplicationContext.registerUser()
        );

        //        Runtime.getRuntime().addShutdownHook(new Thread(applicationContext::close, "application-close"));
        applicationContext.registerShutdownHook();
        applicationContext.refresh();
    }

    @Bean
    public User userByBean() {
        return new User();
    }

    @Bean
    public Student student100() {
        return new Student();
    }
}
