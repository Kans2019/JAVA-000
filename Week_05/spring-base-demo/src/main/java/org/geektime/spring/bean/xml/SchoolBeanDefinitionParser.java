package org.geektime.spring.bean.xml;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author liuhanwei
 * @description
 * @date 2020/11/16
 */
public class SchoolBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element ele) {
        return School.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("class1", parserContext.getDelegate()
                .parseCustomElement((Element) element.getElementsByTagNameNS(element.getNamespaceURI(), "class")
                    .item(0)));
        builder.addPropertyValue("student100", parserContext.getDelegate()
                .parseCustomElement((Element) element.getElementsByTagNameNS(element.getNamespaceURI(), "student")
                        .item(0)));
    }
}
