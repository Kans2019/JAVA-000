package org.geektime.java.client.impl;

import org.geektime.java.client.Request;
import org.geektime.java.client.RequestForward;
import org.junit.Assert;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OkHttpRequestForwardTest {
    private RequestForward requestForward = new OkHttpRequestForward();

    private List<Throwable> list = new ArrayList<>();

    private ExecutorService executor = Executors.newFixedThreadPool(16);

    @Test
    public void sendRequest() throws Throwable {
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