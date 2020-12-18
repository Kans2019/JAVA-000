package org.geektime.java.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.geektime.java.common.Constant;
import org.geektime.java.server.Proxy;
import org.geektime.java.server.ProxyGroup;

import java.util.*;

/**
 * @author Terrdi
 * @description 解析 {@link org.geektime.java.common.Constant#PROXY_XML} 的工具类
 * @date 2020/11/3
 */
public class ProxyResolveUtils {
    private ProxyResolveUtils() {
        throw new UnsupportedOperationException("工具类无法被初始化");
    }

    static {
        HashMap<String, ProxyGroup> hashMap = new HashMap<>();
        try {
            Document document = new SAXReader().read(ProxyResolveUtils.class.getResource("/" + Constant.PROXY_XML));
            Element root = document.getRootElement();
            for (Element element : (List<Element>) root.elements("proxy")) {
                Attribute prefix = element.attribute("prefix");
                List<Element> list = element.elements("host");
                List<Proxy> proxyGroups = new ArrayList<>(list.size());
                for (Element e : list) {
                    Proxy proxy = new Proxy(Integer.valueOf(e.attributeValue("weight", "0")), e.getTextTrim(), prefix.getValue());
                    proxyGroups.add(proxy);
                }
                ProxyGroup group = new ProxyGroup(proxyGroups, element.attributeValue("strategy"));
                hashMap.put(prefix.getValue(), group);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        proxyTable = Collections.unmodifiableMap(hashMap);
    }

    public final static Map<String, ProxyGroup> proxyTable;

    public static void main(String[] args) {
    }
}
