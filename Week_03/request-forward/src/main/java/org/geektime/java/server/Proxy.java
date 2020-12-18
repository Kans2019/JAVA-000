package org.geektime.java.server;

/**
 * @author Terrdi
 * @description 代理服务器pojo 与 {@link org.geektime.java.common.Constant#PROXY_XML} 里的 host 一一对应
 * @date 2020/11/3
 */
public class Proxy {
    private final int weight;

    private final String host;

    private final String prefix;

    public Proxy(int weight, String url, String prefix) {
        this.weight = weight;
        this.host = url;
        this.prefix = prefix;
    }

    public Proxy(String url, String prefix) {
        this(0, url, prefix);
    }

    public int getWeight() {
        return weight;
    }

    public String getHost() {
        return host;
    }

    public String getPrefix() {
        return prefix;
    }
}
