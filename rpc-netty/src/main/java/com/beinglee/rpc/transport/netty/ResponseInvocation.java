package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.InFlightRequests;
import com.beinglee.rpc.transport.ResponseFuture;
import com.beinglee.rpc.transport.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglu
 * @date 2020/6/20 15:08
 */
@Slf4j
@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {

    private final InFlightRequests inFlightRequests;

    public ResponseInvocation(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command response) {
        ResponseFuture future = inFlightRequests.remove(response.getHeader().getRequestId());
        if (future != null) {
            future.getFuture().complete(response);
        } else {
            log.warn("Drop response : {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("ResponseInvocation occur error!", cause);
        super.exceptionCaught(ctx, cause);
        if (ctx.channel().isActive()) {
            ctx.channel().close();
        }
    }
}
