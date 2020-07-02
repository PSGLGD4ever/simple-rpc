package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.RequestHandler;
import com.beinglee.rpc.transport.RequestHandlerRegistry;
import com.beinglee.rpc.transport.command.Command;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanglu
 * @date 2020/6/20 17:14
 */

@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {

    private static final Logger log = LoggerFactory.getLogger(RequestInvocation.class);

    private RequestHandlerRegistry requestHandlerRegistry;

    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command command) throws Exception {
        RequestHandler handler = requestHandlerRegistry.get(command.getHeader().getType());
        if (handler != null) {
            Command response = handler.handle(command);
            if (response != null) {
                ctx.channel().writeAndFlush(response).addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        log.warn("Write response failed!", future.cause());
                    }
                });
            } else {
                log.warn("response is null");
            }
        } else {
            throw new Exception(String.format("No handler for request with type: %d!", command.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("RequestInvocation occur exception!", cause);
        super.exceptionCaught(ctx, cause);
        if (ctx.channel().isActive()) {
            ctx.channel().close();
        }
    }
}
