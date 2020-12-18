package org.geektime.java.client.impl.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.geektime.java.common.Request;
import org.geektime.java.util.ByteBufToBytes;
import org.geektime.java.util.SerialUtils;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * @author Terrdi
 * @description 辅助 {@link org.geektime.java.client.impl.NettyRequestForward} 实现同步发送http请求的handler
 * @date 2020/11/3
 */
public class HttpInBoundHandler<T extends Serializable> extends ChannelInboundHandlerAdapter {
    private final Request<T> request;

    private FullHttpResponse response;

    private ChannelPromise promise;

    private ChannelHandlerContext ctx;

    private ByteBufToBytes reader = null;

    public HttpInBoundHandler(Request<T> request) {
        this.request = request;
    }

    public ChannelPromise sendMessage() {
        byte[] content = this.request.serializeData();
        ByteBuf buf = Unpooled.wrappedBuffer(content);
        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest
                (HttpVersion.valueOf(request.getProtocol().toString()),
                        HttpMethod.valueOf(request.getMethod().getMethod()),
                        request.getNativeUri(), buf);
        fullHttpRequest.headers().add("Host", request.getAddress().getHostName() + ":" + request.getPort());
        fullHttpRequest.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.length);
        this.request.getHeaders().forEach((k, v) -> {
            fullHttpRequest.headers().add(k, v);
        });
        return promise = ctx.writeAndFlush(fullHttpRequest).channel().newPromise();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;

            this.reader = new ByteBufToBytes(HttpUtil.getContentLength(response, 0));
//        System.out.println("content:" + System.getProperty("line.separator") + content.toString(Charset.defaultCharset()));
//        System.out.println("headers:" + System.getProperty("line.separator") + headers.toString());
            this.response = new DefaultFullHttpResponse(response.protocolVersion(), response.status(), response.content(), response.headers(), response.trailingHeaders());
            this.promise.setSuccess();
        } else if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("messageType:"+ request.headers().get("messageType"));
            System.out.println("businessType:"+ request.headers().get("businessType"));
        } else if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();
            if (reader.isEnd()) {
                this.response = new DefaultFullHttpResponse(this.response.protocolVersion(), this.response.status(), Unpooled.wrappedBuffer(reader.readFull()), this.response.headers(), this.response.trailingHeaders());
                ctx.write(response, this.promise);
                ctx.flush();
                this.promise.setSuccess();
            }
        }
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.promise = null;
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    public FullHttpResponse getResponse() {
        return response;
    }
}
