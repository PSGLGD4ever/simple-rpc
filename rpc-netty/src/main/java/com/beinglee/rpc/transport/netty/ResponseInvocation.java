package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.InFlightRequests;
import com.beinglee.rpc.transport.ResponseFuture;
import com.beinglee.rpc.transport.command.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanglu
 * @date 2020/6/20 15:08
 */
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {

    private static final Logger log = LoggerFactory.getLogger(ResponseInvocation.class);

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
