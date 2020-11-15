package org.geektime.spring.bean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 使用BeanDefinition装配Bean
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 * @see GenericApplicationContext#registerBean
 **/
public interface ExtBeanDefinitionApplicationContext {
    static ApplicationContext registerUser() {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        User user = new User("userByBeanDefinition");
        applicationContext.registerBeanDefinition(user.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(User.class, () -> user).getBeanDefinition());
        applicationContext.registerBeanDefinition("logBeanPostProcessor",
                BeanDefinitionBuilder.genericBeanDefinition(LogBeanPostProcessor.class).getBeanDefinition());

        applicationContext.registerShutdownHook();
        applicationContext.refresh();
        return applicationContext;
    }
}
