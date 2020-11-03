package org.geektime.java.client;

import java.io.Closeable;
import java.io.Serializable;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public interface RequestForward<T extends Serializable, R> extends Closeable {
    byte[] sendRequest(Request<T> request);

    Response<R> sendRequestAndResponse(Request<T> request);
}
