package org.geektime.java.impl;

import org.geektime.java.Request;
import org.geektime.java.RequestForward;
import org.junit.Assert;

import java.net.UnknownHostException;

public class HttpClientRequestForwardTest {

    private RequestForward requestForward = new HttpClientRequestForward();

    @org.junit.Test
    public void sendRequest8808() throws UnknownHostException {
        Request<String> request = new Request<String>("192.168.1.7", "HTTP");
        request.setUri("/test");
        request.setMethod(Request.HttpMethod.GET);
        request.setPort(8808);
        Assert.assertEquals("hello,kimmking", new String(requestForward.sendRequest(request)));
    }

    @org.junit.Test
    public void sendRequest8801() throws UnknownHostException {
        Request<String> request = new Request<String>("192.168.1.7", "HTTP");
        request.setMethod(Request.HttpMethod.GET);
        request.setPort(8801);
        Assert.assertEquals("hello,kimming", new String(requestForward.sendRequest(request)));
    }
}