package io.kimmking.rpcfx.api;

public class RpcfxRequest<T> {

    private Class<T> serviceClass;

    private String method;

    private Object[] params;

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = (Class<T>) serviceClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
