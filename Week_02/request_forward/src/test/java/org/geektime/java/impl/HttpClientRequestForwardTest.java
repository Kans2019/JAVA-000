package org.geektime.java.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.geektime.java.Request;
import org.geektime.java.RequestForward;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientRequestForwardTest {

    private RequestForward requestForward = new HttpClientRequestForward();

//    @org.junit.Test
    public void sendRequest8808() throws UnknownHostException {
        Request<String> request = new Request<String>("localhost", "HTTP");
        request.setUri("/test");
        request.setMethod(Request.HttpMethod.GET);
        request.setPort(8808);
        Assert.assertEquals("hello,kimmking", new String(requestForward.sendRequest(request)));
    }

    private List<Throwable> list = new ArrayList<>();

    private ExecutorService executor = Executors.newFixedThreadPool(16);

//    @org.junit.Test
    public void sendRequest8801() throws Throwable {
        Runnable runnable = () -> {
            try {
                Request<String> request = new Request<String>("localhost", "HTTP");
                request.setMethod(Request.HttpMethod.GET);
                request.setPort(8801);
                Assert.assertEquals("hello,nio", new String(requestForward.sendRequest(request)));
            } catch (UnknownHostException e) {
                list.add(e);
            }
        };
        for (int i = 0;i < 100;i++) {
            executor.submit(runnable);
        }
        if (!this.list.isEmpty()) {
            Throwable throwable = this.list.get(0);
            throw throwable;
        }
    }
}