package org.geektime.java.server.filter;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.geektime.java.util.FilterResolveUtils;

/**
 * @author Terrdi
 * @description 过 {@link org.geektime.java.common.Constant#FILTER_XML} 里定义的过滤器
 * @date 2020/11/4
 */
public class FilterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public FilterHandler() {
        super(false);
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        for (HttpRequestFilter filter : FilterResolveUtils.filters) {
            filter.filter(msg, ctx);
        }
        ctx.fireChannelRead(msg);
    }
}
