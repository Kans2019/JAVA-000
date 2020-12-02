package org.geektime.support;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * 判断属性名是否在Spring元数据中定义
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/3
 * @since 1.8
 * @see Environment
 **/
public class ConditionalOnPropertyExistsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnPropertyExists.class.getName());
        String propertyName = (String) annotationAttributes.get("name");
        Environment env = conditionContext.getEnvironment();
        if (Objects.nonNull(env.getProperty(propertyName))) {
            return true;
        }
        if (env instanceof AbstractEnvironment) {
            MutablePropertySources propertySources = ((AbstractEnvironment) env).getPropertySources();
            for (PropertySource propertySource : propertySources) {
                if (propertySource instanceof MapPropertySource) {
                    MapPropertySource map = (MapPropertySource) propertySource;
                    for (String name : map.getPropertyNames()) {
                        if (name.startsWith(propertyName) && !Character.isLetter(name.charAt(propertyName.length()))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
