package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.InFlightRequests;
import com.beinglee.rpc.transport.ResponseFuture;
import com.beinglee.rpc.transport.Transport;
import com.beinglee.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;


/**
 * @author zhanglu
 * @date 2020/6/10 20:09
 */
public class NettyTransport implements Transport {

    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            ResponseFuture responseFuture = new ResponseFuture(request.getHeader().getRequestId(), completableFuture);
            inFlightRequests.put(responseFuture);
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}
