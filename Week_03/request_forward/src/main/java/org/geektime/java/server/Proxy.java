package org.geektime.java.server;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/3
 */
public class Proxy {
    private final int weight;

    private final String url;

    public Proxy(int weight, String url) {
        this.weight = weight;
        this.url = url;
    }

    public Proxy(String url) {
        this(0, url);
    }

    public int getWeight() {
        return weight;
    }

    public String getUrl() {
        return url;
    }
}
