package org.geektime.spring.bean.xml;

import io.kimmking.spring01.Student;
import io.kimmking.spring02.Klass;
import io.kimmking.spring02.School;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class StudentBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private Map<String, Class<?>> map = new HashMap<String, Class<?>>(){{
        put("student", Student.class);
        put("klass", Klass.class);
        put("school", School.class);
    }};


    @Override
    protected Class<?> getBeanClass(Element element) {
        return Student.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String id = element.getAttribute("id");
        if (StringUtils.hasLength(id)) {
            builder.addPropertyValue("id", Integer.valueOf(id));
        }
        String name = element.getAttribute("name");
        if (StringUtils.hasLength(name)) {
            builder.addPropertyValue("name", name);
        }
        String ref = element.getAttribute("ref");
        if (StringUtils.hasLength(ref)) {
            builder.addPropertyReference("", ref);
        }
    }
}
