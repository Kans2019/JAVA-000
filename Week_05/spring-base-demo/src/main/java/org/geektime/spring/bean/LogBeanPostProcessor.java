package org.geektime.spring.bean;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 对Spring装配Bean进行日志打印的类
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class LogBeanPostProcessor implements BeanPostProcessor {
    private final Logger logger = Logger.getLogger(LogBeanPostProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("创建 id 为 %s 的实例对象 %s", beanName, bean));
        }
        return bean;
    }
}
