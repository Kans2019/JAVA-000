package org.geektime.java.util;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.geektime.java.common.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/4
 */
public class FilterResolveUtils {
    private FilterResolveUtils() {
        throw new UnsupportedOperationException();
    }

    public static List<HttpRequestFilter> filters;

    static {
        List<HttpRequestFilter> list = new ArrayList<>();
        try {
            Document document = new SAXReader().read(ProxyResolveUtils.class.getResource("/" + Constant.FILTER_XML));
            Element root = document.getRootElement();
            for (Element element : (List<Element>) root.elements("filter")) {
                String className = element.getTextTrim();
                try {
                    Class clazz = Class.forName(className);
                    if (!HttpRequestFilter.class.isAssignableFrom(clazz)) {
                        throw new RuntimeException(className + " 没有实现HttpRequestFilter接口");
                    }
                    HttpRequestFilter instance = (HttpRequestFilter) clazz.newInstance();
                    list.add(instance);
                } catch (ClassNotFoundException e) {
                    System.err.println("找不到类 " + className);
                } catch (IllegalAccessException | InstantiationException e) {
                    System.err.println(className + " 初始化失败");
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        filters = Collections.unmodifiableList(list);
    }

    public static void main(String[] args) {

    }
}
