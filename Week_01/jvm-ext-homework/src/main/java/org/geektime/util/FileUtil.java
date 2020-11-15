package org.geektime.util;

import java.io.*;
import java.net.URL;

/**
 * @author Terrdi
 * @description 处理文件的工具类
 * @date 2020/11/13
 */
public interface FileUtil {
    /**
     * 读取文件为字节数组
     * @param file
     * @return
     */
    static byte[] read(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            copy(fis, bos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    static byte[] read(URL url) {
        try (InputStream in = url.openStream();
             ByteArrayOutputStream o = new ByteArrayOutputStream()) {
            copy(in, o);
            return o.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
    }
}
