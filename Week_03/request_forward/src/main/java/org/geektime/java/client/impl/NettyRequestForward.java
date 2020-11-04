package org.geektime.java.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.geektime.java.common.Request;
import org.geektime.java.client.RequestForward;
import org.geektime.java.common.Response;
import org.geektime.java.client.impl.netty.handler.HttpInBoundHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/3
 */
public class NettyRequestForward<T extends Serializable> implements RequestForward<T> {
    private EventLoopGroup group = new NioEventLoopGroup();

    @Override
    public Response sendRequestAndResponse(Request<T> request) {
        HttpInBoundHandler<T> httpInBoundHandler = new HttpInBoundHandler<>(request);
        Channel future = resolve(request, httpInBoundHandler);
        ChannelPromise promise = httpInBoundHandler.sendMessage();
        try {
            promise.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            future.close();
        }
        return new Response(httpInBoundHandler.getResponse());
    }

    private <T extends Serializable> Channel resolve(final Request<T> request, final HttpInBoundHandler<T> httpInBoundHandler) {
        Bootstrap client = new Bootstrap();

        client.group(group);

        client.channel(NioSocketChannel.class);
        client.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
//                ch.pipeline().addLast(new ByteArrayEncoder());
                ch.pipeline().addLast(new HttpResponseDecoder());
                ch.pipeline().addLast(new HttpRequestEncoder());
                ch.pipeline().addLast(httpInBoundHandler);
            }
        });
        Channel future = null;
        try {
            future = client.connect(request.getAddress().getHostAddress(), request.getPort()).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return future;
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(group)) group.shutdownGracefully();
    }
}
