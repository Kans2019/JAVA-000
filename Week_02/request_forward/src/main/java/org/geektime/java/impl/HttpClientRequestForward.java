package org.geektime.java.impl;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.geektime.java.Request;
import org.geektime.java.RequestForward;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public class HttpClientRequestForward implements RequestForward, Closeable {
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000)
            .setConnectionRequestTimeout(1000).setSocketTimeout(3000).setExpectContinueEnabled(false).build();
    private static PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).build());

    /**
     * 连接池默认长度
     */
    private static final int DEFAULT_SIZE = 16;

    private Queue<CloseableHttpClient> queue;

    /**
     * 队列长度
     */
    private int capacity;

    public HttpClientRequestForward() {
        this(DEFAULT_SIZE);
    }

    public HttpClientRequestForward(int capacity) {
        queue = new ArrayBlockingQueue<>(this.capacity = capacity);
        for (int i = 0;i < capacity;i++) {
            queue.offer(HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build());
        }
    }

    /**
     * 使用HttpClient发送消息
     * @param request
     * @return
     */
    public byte[] sendRequest(Request request) {
        CloseableHttpClient httpClient = this.queue.poll();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
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
        } finally {
            queue.offer(httpClient);
        }
        return new byte[0];
    }


    private HttpUriRequest resolve(Request request) {
        HttpEntity entity = new ByteArrayEntity(request.serializeData());
        BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest(request.getMethod().getMethod(), request.getUri(), this.resolve(request.getProtocol()));
        httpRequest.setEntity(entity);
        RequestBuilder requestBuilder = RequestBuilder.copy(httpRequest);
        for (Map.Entry<String, String> header : ((Map<String, String>) request.getHeaders()).entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        return requestBuilder.build();
    }

    private ProtocolVersion resolve(Request.Protocol protocol) {
        return new ProtocolVersion(protocol.getName(), protocol.getMajor(), protocol.getMinor());
    }

    @Override
    public synchronized void close() throws IOException {
        IOException ioException = null;
        for (int i = 0;i < this.capacity;i++) {
            CloseableHttpClient httpClient = this.queue.remove();
            try {
                httpClient.close();
            } catch (IOException e) {
                if (ioException == null) {
                    ioException = e;
                }
            }
        }
        if (ioException != null) {
            throw ioException;
        }
    }
}
