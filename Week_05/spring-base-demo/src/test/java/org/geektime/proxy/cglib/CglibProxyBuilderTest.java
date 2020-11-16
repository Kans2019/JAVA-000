package org.geektime.proxy.cglib;

import org.apache.log4j.Logger;
import org.junit.Test;

public class CglibProxyBuilderTest {
    private static final Logger logger = Logger.getLogger(CglibProxyBuilderTest.class);

    static class ProxyTest {
        public void sayHello() {
            System.out.println("Hello World");
        }
    }

    @Test
    public void executeProxy() {
        ProxyTest proxyTest = new ProxyTest();
        proxyTest = new CglibProxyBuilder<>(proxyTest)
                .registerBeforeHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("BeforeHandler 1");
                    }
                    return true;
                }).registerBeforeHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("BeforeHandler 2");
                    }
                    return true;
                }).registerBeforeHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("BeforeHandler 3");
                    }
                    return true;
                }).registerAfterHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("AfterHandler 1");
                    }
                }).registerAfterHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("AfterHandler 2");
                    }
                }).registerAfterHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("AfterHandler 3");
                    }
                }).registerAfterHandler((target, method, argus) -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("AfterHandler 4");
                    }
                })
                .build();

        proxyTest.sayHello();
    }
}
