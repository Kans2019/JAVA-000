package org.geektime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/1
 * @since 1.8
 **/
@SpringBootApplication
public class WriteReadSplitApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(WriteReadSplitApplication.class, args);
//        for (String beanName : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }
    }
}
