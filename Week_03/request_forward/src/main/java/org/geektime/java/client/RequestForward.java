package org.geektime.java.client;

import org.geektime.java.common.Request;
import org.geektime.java.common.Response;

import java.io.Closeable;
import java.io.Serializable;

/**
 * @author Terrdi
 * @description
 * @date 2020/10/27
 */
public interface RequestForward<T extends Serializable> extends Closeable {
    byte[] sendRequest(Request<T> request);

    Response sendRequestAndResponse(Request<T> request);
}
