package org.geektime.java.client.impl.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import org.geektime.java.client.Request;
import org.geektime.java.util.SerialUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/3
 */
public class HttpInBoundHandler<T extends Serializable> extends ChannelInboundHandlerAdapter {
    private final Request<T> request;

    private byte[] response;

    private ChannelPromise promise;

    private ChannelHandlerContext ctx;

    private int readBytes = 0;

    public HttpInBoundHandler(Request<T> request) {
        this.request = request;
    }

    public ChannelPromise sendMessage() {
        ByteBuf buf = Unpooled.wrappedBuffer(this.request.serializeData());
        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest
                (HttpVersion.valueOf(request.getProtocol().toString()),
                        HttpMethod.valueOf(request.getMethod().getMethod()),
                        request.getUri(), buf);
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            fullHttpRequest.headers().add(header.getKey(), header.getValue());
        }
        return promise = ctx.writeAndFlush(fullHttpRequest).channel().newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof FullHttpRequest) {
//            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
//            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
//                fullHttpRequest.headers().add(header.getKey(), header.getValue());
//            }
//            fullHttpRequest.setMethod(HttpMethod.valueOf(request.getMethod().getMethod()));
//            fullHttpRequest.setProtocolVersion(HttpVersion.valueOf(request.getProtocol().toString()));
//            fullHttpRequest.setUri(request.getUri());
//        } else {
//            super.channelRead(ctx, msg);
//        }


        if (msg instanceof String) {
            this.response = ((String) msg).getBytes();
        } else if (msg instanceof Serializable) {
            this.response = SerialUtils.serial((Serializable) msg);
        } else if (msg instanceof DefaultFullHttpResponse) {
            DefaultFullHttpResponse response = (DefaultFullHttpResponse) msg;
            this.response = response.content().array();
        } else if (msg instanceof DefaultHttpResponse) {
            DefaultHttpResponse response = (DefaultHttpResponse) msg;
            if (response.decoderResult().isSuccess()) {
                this.response = new byte[response.headers().getInt(HttpHeaderNames.CONTENT_LENGTH)];
                this.readBytes = 0;
            }
        } else if (Objects.nonNull(this.response) && msg instanceof DefaultLastHttpContent) {
            DefaultLastHttpContent httpContent = (DefaultLastHttpContent) msg;
            ByteBuf buf = httpContent.content();
            if (buf.hasArray()) {
                this.response = buf.array();
            } else {
                buf.readBytes(this.response);
//                buf.getBytes(buf.readerIndex(), this.response);
            }
            promise.setSuccess();
            buf.release();
        } else {
            throw new UnsupportedOperationException("不支持的对象 " + msg);
        }
    }

    public byte[] getResponse() {
        return response;
    }

    private static Map<String, String> proxies = null;

    public static Map<String, String> getProxies() {
        if (Objects.isNull(proxies)) {
            synchronized (HttpInBoundHandler.class) {
                if (Objects.isNull(proxies)) {

                }
            }
        }
        return proxies;
    }
}
