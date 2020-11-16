package org.geektime.spring.bean.xml;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 对有ref属性的{@link org.w3c.dom.Element}先在{@link org.springframework.beans.factory.support.BeanDefinitionRegistry}中查找并返回
 * 如果查找失败再继续执行 {@link AbstractSingleBeanDefinitionParser#parse(Element, ParserContext)}
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/15
 * @since 1.8
 * @see AbstractSingleBeanDefinitionParser#parse(Element, ParserContext)
 **/
public abstract class AbstractRefFirstBeanDefinitionParser extends AbstractBeanDefinitionParser {
    private static final String REF_ATTRIBUTE = "ref";

    private static final Logger logger = Logger.getLogger(AbstractRefFirstBeanDefinitionParser.class);

    private static final Field containingBeanDefinition;

    static {
        try {
            containingBeanDefinition = ParserContext.class.getDeclaredField("containingBeanDefinition");
            containingBeanDefinition.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private AbstractSingleBeanDefinitionParser abstractSingleBeanDefinitionParser = new AbstractSingleBeanDefinitionParser() {
        @Override
        protected Class<?> getBeanClass(org.w3c.dom.Element element) {
            return doGetBeanClass(element);
        }

        @Override
        protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
            executeParse(element, parserContext, builder);
        }
    };

    @Override
    protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        if (element.hasAttribute(REF_ATTRIBUTE)) {
            BeanDefinition beanDefinition = parserContext.getRegistry().getBeanDefinition(element.getAttribute(REF_ATTRIBUTE));
            if (Objects.equals(beanDefinition.getBeanClassName(), this.doGetBeanClass(element).getName())) {
                setNested(parserContext, beanDefinition);
                return (AbstractBeanDefinition) beanDefinition;
            }
        }
        return (AbstractBeanDefinition) abstractSingleBeanDefinitionParser.parse(element, parserContext);
    }

    /**
     * 返回BeanDefinition的类对象
     * @param ele
     * @return
     */
    protected abstract Class<?> doGetBeanClass(Element ele);

    /**
     * Parse the supplied {@link org.w3c.dom.Element} and populate the supplied
     * {@link BeanDefinitionBuilder} as required.
     * <p>The default implementation delegates to the {@code doParse}
     * version without ParserContext argument.
     * @param element the XML element being parsed
     * @param parserContext the object encapsulating the current state of the parsing process
     * @param builder used to define the {@code BeanDefinition}
     * @see #executeParse(org.w3c.dom.Element, BeanDefinitionBuilder)
     */
    protected void executeParse(org.w3c.dom.Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        executeParse(element, builder);
    }

    /**
     * Parse the supplied {@link org.w3c.dom.Element} and populate the supplied
     * {@link BeanDefinitionBuilder} as required.
     * <p>The default implementation does nothing.
     * @param element the XML element being parsed
     * @param builder used to define the {@code BeanDefinition}
     */
    protected void executeParse(org.w3c.dom.Element element, BeanDefinitionBuilder builder) {
    }

    /**
     * 设置结果集到{@link ParserContext}中去
     * @param parserContext
     * @param containBeanDefinition
     * @see ParserContext#isNested()
     */
    private void setNested(ParserContext parserContext, BeanDefinition containBeanDefinition) {
        try {
            containingBeanDefinition.set(parserContext, containBeanDefinition);
        } catch (IllegalAccessException e) {
            logger.warn("设置BeanDefinition失败", e);
        }
    }
}
