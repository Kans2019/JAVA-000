package org.geektime.java.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Objects;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author liuhanwei
 * @description
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

        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK,
                Unpooled.wrappedBuffer(res.getBytes()));
        response.headers()
                .set(CONTENT_TYPE, "text/plain;charset=utf8")
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        ctx.writeAndFlush(response);
    }
}
