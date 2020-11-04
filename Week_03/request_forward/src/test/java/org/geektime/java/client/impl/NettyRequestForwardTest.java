package org.geektime.java.client.impl;

import org.geektime.java.common.Request;
import org.geektime.java.client.RequestForward;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

public class NettyRequestForwardTest {
    private RequestForward<String> requestForward = new NettyRequestForward<>();

    @Test
    public void sendRequest() throws UnknownHostException {
        Request<String> request = new Request<String>("localhost", "HTTP");
        request.setMethod(Request.HttpMethod.GET);
        request.setPort(8080);

        String str = new String(requestForward.sendRequest(request));
        Assert.assertEquals(str, "helloworld");
    }

    @After
    public void close() throws IOException {
        requestForward.close();
    }
}