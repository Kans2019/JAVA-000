package org.geektime.java.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.Objects;

/**
 * @author Terrdi
 * @description 后端服务器处理请求的逻辑
 * @date 2020/11/4
 */
public class SimpleHttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private final String headerName;

    private final int port;

    public SimpleHttpServerHandler(int port, String name) {
        this.headerName = name;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        String res = "%s处理请求, hello, %s";
        String name = request.headers().get(headerName);
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            name = "nio";
        }
        res = String.format(res, this.port, name);

        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(res.getBytes()));
        response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf8")
                .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        ctx.writeAndFlush(response);
    }
}
