package org.geektime.java.client;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/4
 */
public class Response<T> {
    private T response;

    public T getResponse() {
        return response;
    }

    public Response(T r) {
        this.response = r;
    }
}
