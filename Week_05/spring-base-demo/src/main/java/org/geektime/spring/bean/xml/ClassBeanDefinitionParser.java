package org.geektime.spring.bean.xml;

import io.kimmking.spring02.Klass;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * {@link io.kimmking.spring02.Klass} 的xml处理
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class ClassBeanDefinitionParser extends AbstractRefFirstBeanDefinitionParser {
    @Override
    protected Class<?> doGetBeanClass(Element element) {
        return Klass.class;
    }

    @Override
    protected void executeParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("students", parserContext.getDelegate()
                .parseListElement((Element) element.getElementsByTagNameNS(element.getNamespaceURI(), "students").item(0),
                        builder.getBeanDefinition()));
    }
}
