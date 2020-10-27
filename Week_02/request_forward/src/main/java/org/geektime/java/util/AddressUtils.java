package org.geektime.java.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public class AddressUtils {
    private AddressUtils() {}

    /**
     * 解析ip地址
     * @param address
     * @return
     */
    public static InetAddress resolve(String address) throws UnknownHostException {
        int index = 0;
        boolean flag = true;
        StringBuilder sb = new StringBuilder(3);
        byte[] buf = new byte[address.length()];
        for (int i = 0;i < address.length();i++) {
            char ch = address.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                sb.append(ch);
            } else {
                switch (ch) {
                    case '.':
                        flag = false;
                        if (sb.length() == 0) {
                            buf[index++] = 0;
                            break;
                        }
                        // IPv4
                        int value = 256;
                        try {
                            value = Integer.parseInt(sb.toString());
                        } catch (NumberFormatException e) {
                            value = Integer.parseInt(sb.toString(), 16);
                        }
                        if (value > 255) {
                            throw new IllegalArgumentException("Invalid IP Address " + address);
                        }
                        buf[index++] = (byte) value;
                        break;
                    case ':':
                        // IPv6
                        if (sb.length() == 0) {
                            buf[index++] = buf[index++] = 0;
                            break;
                        }
                        value = Integer.parseInt(sb.toString(), 16);
                        buf[index++] = (byte) (value >>> 8);
                        buf[index++] = (byte) value;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid IP address " + address + " at position " + i);
                }
                sb.delete(0, sb.length());
            }
        }
        if (flag) {
            // IPv6
            if (sb.length() == 0) {
                buf[index++] = buf[index++] = 0;
            } else {
                int value = Integer.parseInt(sb.toString(), 16);
                buf[index++] = (byte) (value >>> 8);
                buf[index++] = (byte) value;
            }

        } else {
            if (sb.length() == 0) {
                buf[index++] = 0;
            } else {
                // IPv4
                int value = 256;
                try {
                    value = Integer.parseInt(sb.toString());
                } catch (NumberFormatException e) {
                    value = Integer.parseInt(sb.toString(), 16);
                }
                if (value > 255) {
                    throw new IllegalArgumentException("Invalid IP Address " + address);
                }
                buf[index++] = (byte) value;
            }
        }

        return InetAddress.getByAddress(Arrays.copyOf(buf, index));
    }
}
