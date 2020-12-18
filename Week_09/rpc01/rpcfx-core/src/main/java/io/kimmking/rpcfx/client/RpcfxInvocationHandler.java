package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.http.HttpHeaders;
import org.geektime.java.client.RequestForward;
import org.geektime.java.client.impl.NettyRequestForward;
import org.geektime.java.client.impl.OkHttpRequestForward;
import org.geektime.java.common.Request;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class RpcfxInvocationHandler implements InvocationHandler {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private RequestForward requestForward = new NettyRequestForward();

    private final Class<?> serviceClass;
    private final String url;
    public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        RpcfxRequest<?> request = new RpcfxRequest<>();
        request.setServiceClass(this.serviceClass);
        request.setMethod(method.getName());
        request.setParams(params);

        RpcfxResponse response = null;
        try {
            response = post(request, url);
        } catch (IOException e) {
            throw new RpcfxException(e);
        }

        if (response.isStatus()) {
            return JSON.parseObject(response.getResult().toString(), method.getReturnType());
        } else {
            throw response.getException();
        }

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException


    }

    private RpcfxResponse post(RpcfxRequest<?> req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        org.geektime.java.common.Request<String> request = new org.geektime.java.common.Request<>(url);
        request.setMethod(Request.HttpMethod.POST);
        request.addHeader(HttpHeaders.CONTENT_TYPE, JSONTYPE.toString());
        request.setData(reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
//            OkHttpClient client = new OkHttpClient();
//            final Request request = new Request.Builder()
//                    .url(url)
//                    .post(RequestBody.create(JSONTYPE, reqJson))
//                    .build();
//            String respJson = client.newCall(request).execute().body().string();
        String respJson = new String(requestForward.sendRequestAndResponse(request).getContent());
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
