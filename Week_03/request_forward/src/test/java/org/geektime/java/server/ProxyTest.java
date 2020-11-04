package org.geektime.java.server;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.geektime.java.client.RequestForward;
import org.geektime.java.client.impl.HttpClientRequestForward;
import org.geektime.java.common.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Terrdi
 * @description
 * @date 2020/11/4
 */
public class ProxyTest {
    ExecutorService executors = null;

    private ProxyServer proxyServer = new ProxyServer(8089);

    private RequestForward requestForward = new HttpClientRequestForward();

    @Before
    public void createServer() throws InterruptedException {
        executors = new ThreadPoolExecutor(10, 100,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                new BasicThreadFactory.Builder().namingPattern("server-%d")
                        .daemon(true).build());
        List<SimpleHttpServer> list = new ArrayList<>();
        for (int i = 8080; i < 8085; i++) {
            list.add(new SimpleHttpServer(i));
        }
        list.forEach(executors::execute);

        executors.execute(this.proxyServer::start);

        Thread.sleep(3000);
    }

    @After
    public void release() throws IOException {
        proxyServer.close();
        executors.shutdown();
    }

    @Test
    public void proxyTest() throws UnknownHostException {
        Request<String> nio01 = new Request<>("http://localhost:8089/nio01");
        Request<String> nio02 = new Request<>("http://localhost:8089/nio02");
        Request<String> nio03 = new Request<>("http://localhost:8089/nio03");

        System.out.println("权重策略 nio01=============>");
        for (int i = 0; i < 10; i++) {
            System.out.println(new String(requestForward.sendRequest(nio01)));
        }

        System.out.println("随机策略 nio02=============>");
        for (int i = 0; i < 10; i++) {
            System.out.println(new String(requestForward.sendRequest(nio02)));
        }

        System.out.println("轮询策略 nio03=============>");
        for (int i = 0; i < 10; i++) {
            System.out.println(new String(requestForward.sendRequest(nio03)));
        }
    }
}
