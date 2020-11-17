package org.geektime.spring.bean.xml;

import io.kimmking.spring01.Student;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * {@link io.kimmking.spring01.Student} 的xml处理
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 **/
public class StudentBeanDefinitionParser extends AbstractRefFirstBeanDefinitionParser {
    @Override
    protected Class<?> doGetBeanClass(Element ele) {
        return Student.class;
    }

    @Override
    protected void executeParse(Element element, BeanDefinitionBuilder builder) {
        String id = element.getAttribute("id");
        if (StringUtils.hasLength(id)) {
            builder.addPropertyValue("id", Integer.valueOf(id));
        }
        String name = element.getAttribute("name");
        if (StringUtils.hasLength(name)) {
            builder.addPropertyValue("name", name);
        }
    }
}
