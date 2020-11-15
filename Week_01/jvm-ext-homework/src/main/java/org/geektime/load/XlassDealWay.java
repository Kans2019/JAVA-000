package org.geektime.load;

/**
 * @author Terrdi
 * @description 处理 byte 数组的方法
 * @date 2020/11/13
 */
@FunctionalInterface
public interface XlassDealWay {
    /**
     * 对byte数组进行转换
     * @param bytes
     * @return
     */
    byte[] resolve(byte[] bytes);
}
