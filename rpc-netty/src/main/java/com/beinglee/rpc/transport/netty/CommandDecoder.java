package com.beinglee.rpc.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zhanglu
 * @date 2020/6/28 10:21
 */
public abstract class CommandDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {

    }

    protected void decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {

    }
}
