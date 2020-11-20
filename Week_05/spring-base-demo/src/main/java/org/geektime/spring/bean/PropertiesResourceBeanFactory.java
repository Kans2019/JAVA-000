package org.geektime.spring.bean;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * 通过 Properties 示例化 BeanDefinition
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/20
 * @since 1.8
 * @see
 **/
public class PropertiesResourceBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);

        // 通过UTF-8 编码加载配置文件
        Resource resource = new ClassPathResource("spring.properties");
        int beanCount = reader.loadBeanDefinitions(new EncodedResource(resource));

        System.out.println(beanFactory.getBean("user"));
    }
}
