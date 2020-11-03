package org.geektime.java.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.geektime.java.client.Request;
import org.geektime.java.client.RequestForward;
import org.geektime.java.server.strategy.ProxyStrategy;
import org.geektime.java.util.ProxyResolveUtils;

import java.util.Map;


/**
 * @author Terrdi
 * @description
 * @date 2020/11/3
 */
public class ProxyHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final RequestForward requestForward;

    public ProxyHandler(RequestForward requestForward) {
        this.requestForward = requestForward;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            Map<String, ProxyGroup> proxyGroupMap = ProxyResolveUtils.proxyTable;
            boolean flag = true;
            for (String key : proxyGroupMap.keySet()) {
                if (uri.startsWith(key) || uri.startsWith("/" + key)){
                    ProxyGroup proxyGroup = proxyGroupMap.get(key);
                    Proxy proxy = ProxyStrategy.strategyTable.get(proxyGroup.getStrategy()).getNext(proxyGroup.getCollection());
                    request.headers().set(HttpHeaderNames.HOST, proxy.getUrl());
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ctx.writeAndFlush(
                        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("Not Found".getBytes())));
                return;
            }
            System.out.println(request);
            Request<?> request1 = new Request<>(request);
            CloseableHttpResponse response = (CloseableHttpResponse) requestForward.sendRequestAndResponse(request1).getResponse();
            byte[] body = EntityUtils.toByteArray(response.getEntity());
            FullHttpResponse result =
                    new DefaultFullHttpResponse(HttpVersion.valueOf(response.getProtocolVersion().toString()),
                            HttpResponseStatus.valueOf(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()),
                            Unpooled.wrappedBuffer(body));
            for (Header header : response.getAllHeaders()) {
                result.headers().add(header.getName(), header.getValue());
            }

            System.out.println(result.toString());

            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
