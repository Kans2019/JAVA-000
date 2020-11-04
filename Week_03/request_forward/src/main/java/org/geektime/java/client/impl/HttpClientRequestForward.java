package org.geektime.java.client.impl;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HTTP;
import org.geektime.java.common.Request;
import org.geektime.java.client.RequestForward;
import org.geektime.java.common.Response;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public class HttpClientRequestForward implements RequestForward<Serializable>, Closeable {
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

    @Override
    public Response sendRequestAndResponse(Request request) {
        CloseableHttpClient httpClient = this.queue.poll();
        try {
            return new Response(httpClient.execute(resolve(request)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            queue.offer(httpClient);
        }
        return null;
    }

    /**
     * 默认的 Content-Type
     */
    private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.create("text/plain", Charset.defaultCharset());


    private HttpUriRequest resolve(Request<Serializable> request) {
        HttpEntity entity = null;
        if (request != null && Objects.nonNull(request.getData())) {
            if (request.getData() instanceof String) {
                entity = new StringEntity(String.valueOf(request.getData()),
                        ContentType.parse(request.getHeaders().getOrDefault(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE.toString())));
            } else {
                entity = new SerializableEntity(request.getData());
            }
        }

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
