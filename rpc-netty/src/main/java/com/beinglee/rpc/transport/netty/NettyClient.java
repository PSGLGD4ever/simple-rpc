package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.InFlightRequests;
import com.beinglee.rpc.transport.Transport;
import com.beinglee.rpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanglu
 * @date 2020/6/20 14:31
 */
public class NettyClient implements TransportClient {

    private final InFlightRequests inFlightRequests;

    private EventLoopGroup ioEventGroup;

    private Bootstrap bootstrap;

    private List<Channel> channels;

    public NettyClient() {
        this.inFlightRequests = new InFlightRequests();
        this.channels = new LinkedList<>();
    }

    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        Channel channel = this.createChannel(address, connectionTimeout);
        return new NettyTransport(channel, inFlightRequests);
    }

    private synchronized Channel createChannel(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("socketAddress is null");
        }
        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null) {
            bootstrap = newBootstrap();
        }
        ChannelFuture channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException();
        }
        Channel channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);
        return channel;
    }

    private EventLoopGroup newIoEventGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    private Bootstrap newBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(newChannelHandlerPipeline());
        return bootstrap;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(new RequestEncoder());
                channel.pipeline().addLast(new ResponseDecoder());
                channel.pipeline().addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }

    @Override
    public void close() {
        channels.forEach(
                channel -> {
                    if (channel != null) {
                        channel.close();
                    }
                }
        );
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        inFlightRequests.close();
    }
}
