package org.geektime.java.impl;

import okhttp3.*;
import okio.BufferedSink;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.geektime.java.RequestForward;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author liuhanwei
 * @description
 * @date 2020/10/28
 */
public class OkHttpRequestForward implements RequestForward, AutoCloseable {
    private Queue<OkHttpClient> queue;

    /**
     * 连接池默认长度
     */
    private static final int DEFAULT_SIZE = 16;

    /**
     * 队列长度
     */
    private int capacity;

    public OkHttpRequestForward() {
        this(DEFAULT_SIZE);
    }

    public OkHttpRequestForward(int capacity) {
        queue = new ArrayBlockingQueue<>(this.capacity = capacity);
        for (int i = 0;i < capacity;i++) {
            queue.offer(new OkHttpClient());
        }
    }


    @Override
    public byte[] sendRequest(org.geektime.java.Request request) {
        OkHttpClient client = this.queue.poll();
        try (Response response = client.newCall(this.resolve(request)).execute()) {
            return response.body().bytes();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            this.queue.offer(client);
        }
        return new byte[0];
    }

    private Request resolve(org.geektime.java.Request request) {
        Request.Builder builder = new Request.Builder().url(request.getUri());
        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.get(((Map<String, String>) request.getHeaders())
                        .getOrDefault(HTTP.CONTENT_TYPE, "text/plain"));
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(request.serializeData());
            }
        };
        for (Map.Entry<String, String> header : ((Map<String, String>) request.getHeaders()).entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }


        return builder.method(request.getMethod().getMethod(), body).build();
    }

    @Override
    public synchronized void close() throws Exception {
        IOException ioException = null;
        for (int i = 0;i < this.capacity;i++) {
            OkHttpClient httpClient = this.queue.remove();
        }
        if (ioException != null) {
            throw ioException;
        }
    }
}
