package org.geektime.spring.bean.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class StudentBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        NamedNodeMap nodes = element.getAttributes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            builder.addPropertyValue(node.getNodeName(), node.getNodeValue());
        }
    }
}
