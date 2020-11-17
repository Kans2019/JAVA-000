package org.geektime.support;

import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/18
 * @since 1.8
 **/
@Component
public class IgnoreBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (Objects.nonNull(this.beanFactory)) {
            try {
                MethodMetadataReadingVisitor readingVisitor = (MethodMetadataReadingVisitor) ((DefaultListableBeanFactory) this.beanFactory).getBeanDefinition(beanName).getSource();
                if (Objects.nonNull(readingVisitor) && readingVisitor.isAnnotated(IgnoreBean.class.getTypeName())) {
                    return !readingVisitor.getAnnotationAttributes(IgnoreBean.class.getTypeName()).getBoolean("ignoreAutowiredPostProcessor");
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        return super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }
}
