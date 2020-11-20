package org.geektime.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 使用{@link Configuration} 装配SpringBean
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 * @see Configuration
 * @see org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
 **/
@Configuration("userByConfiguration")
public class User implements ApplicationContextAware, BeanPostProcessor {
    private Long id = System.nanoTime();

    @Value("byAnnotation")
    private String name;

    public User() {}

    public User(String name) {
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Aware 接口生效");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor 接口生效");
        return bean;
    }
}
