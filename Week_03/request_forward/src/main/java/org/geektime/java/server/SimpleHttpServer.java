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
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.geektime.java.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/4
 */
public class SimpleHttpServer extends Thread {
    /**
     * 监听的端口
     */
    private final int port;

    public SimpleHttpServer(int port) {
        this.port = port;
    }

    public void run() {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpServerExpectContinueHandler());
                            p.addLast(new HttpObjectAggregator(1024 * 1024));
                            p.addLast(new SimpleHttpServerHandler(port, Constant.NAME_HEADER));
                        }
                    });

            Channel ch = b.bind(this.port).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    "http://127.0.0.1:" + this.port);

            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executors = new ThreadPoolExecutor(5, 100,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                new BasicThreadFactory.Builder().namingPattern("server-%d")
                .daemon(true).build());
        List<SimpleHttpServer> list = new ArrayList<>();
        for (int i = 8080; i < 8085; i++) {
            list.add(new SimpleHttpServer(i));
        }
        list.forEach(executors::execute);

        executors.awaitTermination(1, TimeUnit.HOURS);
    }
}
