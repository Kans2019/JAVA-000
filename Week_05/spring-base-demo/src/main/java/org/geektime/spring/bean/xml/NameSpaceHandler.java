package org.geektime.spring.bean.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class NameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("student", new StudentBeanDefinitionParser());
        registerBeanDefinitionParser("class", new ClassBeanDefinitionParser());
        registerBeanDefinitionParser("school", new SchoolBeanDefinitionParser());
    }
}
