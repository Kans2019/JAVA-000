package org.geektime.load;

import org.geektime.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Terrdi
 * @description 加载xlass文件为类 xlass 文件的每个字节 和 class 文件的每个字节相加为 255
 * @date 2020/11/13
 */
public class XlassLoader extends URLClassLoader {
    private final XlassDealWay DEFAULT_DEAL_WAY = b -> b;

    public XlassLoader(final ClassLoader classLoader) {
        super(new URL[0], classLoader);
        ClassLoader parent = classLoader;
        while (Objects.nonNull(parent)) {
            if (parent instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) parent;
                for (URL url : urlClassLoader.getURLs()) {
                    this.registerXlassPath(url.getPath());
                    super.addURL(url);
                }
            }
            parent = parent.getParent();
        }
    }

    public XlassLoader() {
        this(XlassLoader.class.getClassLoader());
    }

    /**
     * xlassPath 目录
     */
    LinkedList<String> xlassPaths = new LinkedList<>();

    /**
     * 不同的文件后缀对应不同的处理方式
     */
    Map<String, XlassDealWay> dealWays = new HashMap<>();

    /**
     * 注册需要扫描的路径
     * @param paths
     * @return
     */
    public XlassLoader registerXlassPath(String... paths) {
        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                path = file.getAbsolutePath();
            } else if (file.isFile()) {
                try {
                    URL url = file.toURI().toURL();
                    super.addURL(url);
                    path = url.toString();
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("不符合要求的路径 " + path);
                }
            }
            this.xlassPaths.addFirst(path);
        }

        return this;
    }

    /**
     * 注册需要扫描的文件后缀和加载方式
     * @param xlassDealWay
     * @param suffixes
     * @return
     */
    public XlassLoader registerXlassSuffix(XlassDealWay xlassDealWay, String... suffixes) {
        for (String suffix : suffixes) {
            this.registerXlassSuffix(suffix, xlassDealWay);
        }
        return this;
    }

    /**
     * 注册需要扫描的文件后缀并使用默认加载方式{@link XlassLoader#DEFAULT_DEAL_WAY}
     * @param suffixes
     * @return
     */
    public XlassLoader registerXlassSuffix(String... suffixes) {
        return registerXlassSuffix(DEFAULT_DEAL_WAY, suffixes);
    }

    /**
     * 注册需要扫描的文件后缀
     * @param suffix 需要注册的后缀
     * @return
     */
    public XlassLoader registerXlassSuffix(String suffix, XlassDealWay xlassDealWay) {
        XlassDealWay dealWay = dealWays.get(suffix);
        if (Objects.nonNull(dealWay)) {
            throw new IllegalArgumentException("已注册的后缀处理方式");
        }
        dealWays.put(suffix, xlassDealWay);
        return this;
    }

    /**
     * 调用 {@link ClassLoader#loadClass(String)} 找不到时调用该方法
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        try {
            for (String suffix : this.dealWays.keySet()) {
                Enumeration<URL> us = super.getResources(name.concat(".").concat(suffix));
                if (us.hasMoreElements()) {
                    URL url = us.nextElement();
                    byte[] classBytes = this.dealWays.get(suffix).resolve(FileUtil.read(url));
                    return super.defineClass(name, classBytes, 0, classBytes.length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
}
