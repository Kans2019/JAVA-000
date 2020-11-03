package org.geektime.java.util;

import java.io.*;
import java.util.logging.Logger;

/**
 * @author Terrdi
 * @description 序列化相关接口
 * @date 2020/11/3
 */
public class SerialUtils {
    private static final Logger logger = Logger.getLogger("SerialUtils");


    private SerialUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 序列化
     * @param obj
     * @return
     */
    public static byte[] serial(Serializable obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /**
     * 反系列化
     * @param bytes
     * @return
     */
    public static Serializable deserial(byte[] bytes) {
        try (ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
             ObjectInputStream oos = new ObjectInputStream(baos)) {
            return (Serializable) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
