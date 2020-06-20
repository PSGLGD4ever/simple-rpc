package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.RequestHandlerRegistry;
import com.beinglee.rpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author zhanglu
 * @date 2020/6/20 14:32
 */
public class NettyServer implements TransportServer {

    private RequestHandlerRegistry requestHandlerRegistry;

    private EventLoopGroup acceptEventGroup;

    private EventLoopGroup ioEventGroup;

    private Channel channel;

    @Override
    public void start(RequestHandlerRegistry registry, int port) throws Exception {
        this.requestHandlerRegistry = registry;
        this.acceptEventGroup = newEventLoopGroup();
        this.ioEventGroup = newEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(acceptEventGroup, ioEventGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .handler(newChannelHandlerPipeline());
        this.channel = doBind(serverBootstrap, port);
    }

    private EventLoopGroup newEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(CommandCodecHandler.getInstance());
                channel.pipeline().addLast(new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    private Channel doBind(ServerBootstrap serverBootstrap, int port) throws InterruptedException {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }

    @Override
    public void stop() {
        if (acceptEventGroup != null) {
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }
}
