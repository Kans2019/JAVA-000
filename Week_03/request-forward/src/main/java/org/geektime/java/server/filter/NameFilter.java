package org.geektime.java.server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.geektime.java.common.Constant;

/**
 * @author Terrdi
 * @description 增加文件头过滤器
 * @date 2020/11/4
 */
public class NameFilter implements io.github.kimmking.gateway.filter.HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().add(Constant.NAME_HEADER, System.getProperty("user.name"));
    }
}
