package org.geektime.java.client;

import org.geektime.java.common.Request;
import org.geektime.java.common.Response;

import java.io.Closeable;
import java.io.Serializable;

/**
 * @author Terrdi
 * @description 请求接口类
 * @date 2020/10/27
 */
public interface RequestForward<T extends Serializable> extends Closeable {
    /**
     * 发送请求并获取返回的消息的字节数组
     * @param request
     * @return
     */
    default byte[] sendRequest(Request<T> request) {
        Response response = this.sendRequestAndResponse(request);
        return response.getContent();
    }

    /**
     * 发送请求并获取返回对象
     * @param request
     * @return
     */
    Response sendRequestAndResponse(Request<T> request);
}
