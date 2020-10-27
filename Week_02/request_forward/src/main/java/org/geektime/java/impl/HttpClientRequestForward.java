package org.geektime.java.impl;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.geektime.java.Request;
import org.geektime.java.RequestForward;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public class HttpClientRequestForward implements RequestForward {
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000)
            .setConnectionRequestTimeout(1000).setSocketTimeout(3000).setExpectContinueEnabled(false).build();
    private static PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).build());
    private CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
            .setConnectionManager(connectionManager)
            .build();

    /**
     * 使用HttpClient发送消息
     * @param request
     * @return
     */
    public byte[] sendRequest(Request request) {

        try (CloseableHttpClient httpClient = HttpClients.createSystem();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
             CloseableHttpResponse response = httpClient.execute(resolve(request))
             ) {
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (Objects.nonNull(entity)) {
                    entity.writeTo(bos);
                }
                return bos.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private HttpUriRequest resolve(Request request) {
        HttpEntity entity = new ByteArrayEntity(request.serializeData());
        BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest(request.getMethod().getMethod(), request.getUri(), this.resolve(request.getProtocol()));
        httpRequest.setEntity(entity);
        return RequestBuilder.copy(httpRequest)
                .addHeader("Connection", "close")
                .addHeader("KeepAliveRequests", "0")
                .addHeader("KeepAliveTimeout", "0")
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)").build();
    }

    private ProtocolVersion resolve(Request.Protocol protocol) {
        return new ProtocolVersion(protocol.getName(), protocol.getMajor(), protocol.getMinor());
    }
}
