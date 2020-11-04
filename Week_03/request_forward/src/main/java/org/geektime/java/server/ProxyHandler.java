package org.geektime.java.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.geektime.java.common.Request;
import org.geektime.java.client.RequestForward;
import org.geektime.java.common.Response;
import org.geektime.java.server.strategy.ProxyStrategy;
import org.geektime.java.util.ProxyResolveUtils;

import java.util.Map;
import java.util.Objects;


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
            Proxy proxy = resolve(uri);
            if (Objects.isNull(proxy)) {
                ctx.writeAndFlush(
                        new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("Not Found".getBytes())));
                return;
            }
            request.headers().set(HttpHeaderNames.HOST, proxy.getHost());
            int index = uri.indexOf(proxy.getPrefix()) + proxy.getPrefix().length();
            uri = uri.substring(index);
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            request.setUri(uri);
            Request<?> request1 = new Request<>(request);
            Response response = requestForward.sendRequestAndResponse(request1);
            FullHttpResponse result = parse(response);

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

    private static FullHttpResponse parse(Response response) {
        final FullHttpResponse httpResponse =
                new DefaultFullHttpResponse(HttpVersion.valueOf(response.getProtocol().toString()),
                        HttpResponseStatus.valueOf(response.getStatusCode(), response.getReasonPhrase()),
                        Unpooled.wrappedBuffer(response.getContent()));
        response.getHeaders().forEach((k, v) -> httpResponse.headers().add(String.valueOf(k), v));
        return httpResponse;
    }

    private static Proxy resolve(String uri) {
        Map<String, ProxyGroup> proxyGroupMap = ProxyResolveUtils.proxyTable;
        for (String key : proxyGroupMap.keySet()) {
            if (uri.startsWith(key) || uri.startsWith("/" + key)){
                ProxyGroup proxyGroup = proxyGroupMap.get(key);
                return ProxyStrategy.strategyTable.get(proxyGroup.getStrategy()).getNext(proxyGroup.getCollection());
            }
        }
        return null;
    }
}
