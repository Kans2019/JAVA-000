package org.geektime.java.client.impl;

import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okio.BufferedSink;
import org.apache.http.protocol.HTTP;
import org.geektime.java.client.RequestForward;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Terrdi
 * @description 基于 OkHttp 来实现的发送请求类
 * @date 2020/10/28
 */
public class OkHttpRequestForward implements RequestForward<Serializable> {
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
    public byte[] sendRequest(org.geektime.java.common.Request<Serializable> request) {
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

    @Override
    public org.geektime.java.common.Response sendRequestAndResponse(org.geektime.java.common.Request<Serializable> request) {
        OkHttpClient client = this.queue.poll();
        try (Response response = client.newCall(this.resolve(request)).execute()) {
            return new org.geektime.java.common.Response(response);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            this.queue.offer(client);
        }
        return null;
    }

    private Request resolve(org.geektime.java.common.Request request) {
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


        return builder.method(request.getMethod().getMethod(), HttpMethod.permitsRequestBody(request.getMethod().getMethod()) ? body : null).build();
    }

    @Override
    public synchronized void close() throws IOException{
        IOException ioException = null;
        for (int i = 0;i < this.capacity;i++) {
            OkHttpClient httpClient = this.queue.remove();
        }
        if (ioException != null) {
            throw ioException;
        }
    }
}
