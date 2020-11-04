package org.geektime.java.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.geektime.java.client.impl.HttpClientRequestForward;
import org.geektime.java.server.filter.FilterHandler;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/3
 */
public class ProxyServer implements Closeable {
    /**
     * 监听的端口
     */
    private final int port;

    // Configure the server.
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public ProxyServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpServerExpectContinueHandler());
                            p.addLast(new HttpObjectAggregator(1024 * 1024));
                            p.addLast(new FilterHandler());
                            p.addLast(new ProxyHandler(new HttpClientRequestForward()));
                        }
                    });

            Channel ch = b.bind(this.port).sync().channel();

            System.err.println("proxy server start at " +
                    "http://127.0.0.1:" + this.port);

            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
