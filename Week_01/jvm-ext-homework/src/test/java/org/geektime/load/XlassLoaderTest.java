package org.geektime.load;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class XlassLoaderTest {

    @Test
    public void loadClass() throws Exception {
        XlassLoader loader = new XlassLoader();
        loader.registerXlassSuffix(b -> {
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (0xFF - b[i]);
            }
            return b;
        }, "xlass");

        Class clazz = loader.loadClass("Hello");
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(obj);

        // 卸载
        method = null;
        obj = null;
        clazz = null;
        loader = null;

        System.gc();
    }

    @Test
    public void loadJar() throws Exception {
        XlassLoader loader = new XlassLoader(null);
        loader.registerXlassSuffix(b -> {
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (0xFF - b[i]);
            }
            return b;
        }, "xlass");

        loader.registerXlassPath("src/test/resources/hello.xar");

        Class clazz = loader.loadClass("Hello");
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(obj);

        // 卸载
        method = null;
        obj = null;
        clazz = null;
        loader = null;

        System.gc();
    }
}