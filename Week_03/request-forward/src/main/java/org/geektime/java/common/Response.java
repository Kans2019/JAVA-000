package org.geektime.java.common;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Terrdi
 * @description 返回包装类
 * @date 2020/11/4
 */
public class Response {
    private final int statusCode;

    private final String reasonPhrase;

    private final Map<String, Object> headers;

    private final byte[] content;

    private final Request.Protocol protocol;

    public Response(CloseableHttpResponse httpResponse) throws IOException {
        this.statusCode = httpResponse.getStatusLine().getStatusCode();
        this.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        this.protocol = Request.Protocol.resolve(httpResponse.getProtocolVersion());
        this.content = EntityUtils.toByteArray(httpResponse.getEntity());
        Map<String, Object> map = new HashMap<>();
        for (Header header : httpResponse.getAllHeaders()) {
            map.put(header.getName(), header.getValue());
        }
        this.headers = Collections.unmodifiableMap(map);
    }

    public Response(okhttp3.Response response) throws IOException {
        this.statusCode = response.code();
        this.reasonPhrase = response.message();
        this.content = response.body().bytes();
        this.protocol = Request.Protocol.resolve(response.protocol().name()).orElseThrow(() -> new IllegalArgumentException("不合法的协议 " + response.protocol().toString()));
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, List<String>> header : response.headers().toMultimap().entrySet()) {
            map.put(header.getKey(), String.join(",", header.getValue()));
        }
        this.headers = Collections.unmodifiableMap(map);
    }

    public Response(DefaultFullHttpResponse httpResponse) {
        this.statusCode = httpResponse.status().code();
        this.reasonPhrase = httpResponse.status().reasonPhrase();
        this.content = httpResponse.content().array();
        this.protocol = Request.Protocol.resolve(httpResponse.protocolVersion());
        Map<String, Object> map = new HashMap<>();
        httpResponse.headers().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        httpResponse.trailingHeaders().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        this.headers = Collections.unmodifiableMap(map);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public Request.Protocol getProtocol() {
        return protocol;
    }
}
