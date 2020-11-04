package org.geektime.java.client.impl.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import org.geektime.java.common.Request;
import org.geektime.java.util.SerialUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Terrdi
 * @description 辅助 {@link org.geektime.java.client.impl.NettyRequestForward} 实现同步发送http请求的handler
 * @date 2020/11/3
 */
public class HttpInBoundHandler<T extends Serializable> extends ChannelInboundHandlerAdapter {
    private final Request<T> request;

    private byte[] content;

    private DefaultFullHttpResponse response;

    private ChannelPromise promise;

    private ChannelHandlerContext ctx;

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
        if (msg instanceof String) {
            this.content = ((String) msg).getBytes();
        } else if (msg instanceof Serializable) {
            this.content = SerialUtils.serial((Serializable) msg);
        } else if (msg instanceof DefaultFullHttpResponse) {
            this.response = (DefaultFullHttpResponse) msg;
        } else if (msg instanceof DefaultHttpResponse) {
            DefaultHttpResponse response = (DefaultHttpResponse) msg;
            if (response.decoderResult().isSuccess()) {
                this.content = new byte[response.headers().getInt(HttpHeaderNames.CONTENT_LENGTH)];
                this.response = new DefaultFullHttpResponse(response.protocolVersion(), response.status(),
                        Unpooled.buffer(0), response.headers(), response.headers());
            }
        } else if (Objects.nonNull(this.response) && msg instanceof DefaultLastHttpContent) {
            DefaultLastHttpContent httpContent = (DefaultLastHttpContent) msg;
            ByteBuf buf = httpContent.content();
            if (buf.hasArray()) {
                this.content = buf.array();
            } else {
                buf.readBytes(this.content);
            }
            ByteBuf byteBuf = Unpooled.buffer(this.content.length);
            byteBuf.writeBytes(this.content);
            byteBuf.retain();
            this.response = new DefaultFullHttpResponse(
                    this.response.protocolVersion(),
                    this.response.status(),
                    byteBuf,
                    this.response.headers(),
                    httpContent.trailingHeaders()
            );
            buf.release();
            this.promise.setSuccess();
            ctx.writeAndFlush(this.response);
        } else {
            throw new UnsupportedOperationException("不支持的对象 " + msg);
        }
    }

    public byte[] getContent() {
        return Objects.isNull(this.content) ? this.response.content().array() : this.content;
    }

    public DefaultFullHttpResponse getResponse() {
        return response;
    }
}
