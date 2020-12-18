package org.geektime.java.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.geektime.java.common.Request;
import org.geektime.java.client.RequestForward;
import org.geektime.java.common.Response;
import org.geektime.java.client.impl.netty.handler.HttpInBoundHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Terrdi
 * @description 基于 Netty 来实现的发送请求类
 * @date 2020/11/3
 * @see org.geektime.java.client.impl.netty.handler.HttpInBoundHandler
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
//                ch.pipeline().addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
//                ch.pipeline().addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
                //包含编码器和解码器
//                ch.pipeline().addLast(new HttpClientCodec());

                //聚合
//                ch.pipeline().addLast(new HttpObjectAggregator(1024 * 10 * 1024));

                //解压
//                ch.pipeline().addLast(new HttpContentDecompressor());

//                ch.pipeline().addLast(new HttpResponseDecoder());
//                ch.pipeline().addLast(new HttpRequestEncoder());
                ChannelPipeline p = ch.pipeline();
                p.addLast(new HttpClientCodec());

                // Remove the following line if you don't want automatic content
                // decompression.
                p.addLast(new HttpContentDecompressor());//这里要添加解压，不然打印时会乱码

                // Uncomment the following line if you don't want to handle
                // HttpContents.
                // p.addLast(new HttpObjectAggregator(1048576));
                p.addLast(new HttpObjectAggregator(123433));

                ch.pipeline().addLast(httpInBoundHandler);
//                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,4,4,-8,0));
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
