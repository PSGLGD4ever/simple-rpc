package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhanglu
 * @date 2020/6/28 9:48
 */
@ChannelHandler.Sharable
public class RequestEncoder extends CommandEncoder {

    private static RequestEncoder instance = null;

    private RequestEncoder() {
    }

    public static RequestEncoder getInstance() {
        if (instance == null) {
            instance = new RequestEncoder();
        }
        return instance;
    }

    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        super.encodeHeader(ctx, header, byteBuf);
    }
}
