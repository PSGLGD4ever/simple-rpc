package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Command;
import com.beinglee.rpc.transport.command.Header;
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
        if (!byteBuf.isReadable(Command.LENGTH_FIELD_LENGTH)) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - Command.LENGTH_FIELD_LENGTH;
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        Header header = decodeHeader(ctx, byteBuf, out);
        int payloadLength = length - header.length();
        byte[] payload = new byte[payloadLength];
        byteBuf.readBytes(payload);
        Command command = new Command(header, payload);
        out.add(command);
    }

    protected abstract Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out);
}
