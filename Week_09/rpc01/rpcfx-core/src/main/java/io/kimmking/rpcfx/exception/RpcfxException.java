package io.kimmking.rpcfx.exception;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/16
 * @since 1.8
 **/
public class RpcfxException extends RuntimeException {
    public RpcfxException() {
        super();
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcfxException(Throwable cause) {
        super(cause);
    }

    protected RpcfxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
