package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Command;
import com.beinglee.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zhanglu
 * @date 2020/6/28 9:54
 */
public abstract class CommandEncoder extends MessageToByteEncoder<Command> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, ByteBuf byteBuf) {
        // 数据总长度
        byteBuf.writeInt(Command.LENGTH_FIELD_LENGTH + command.getHeader().length() + command.getPayload().length);
        encodeHeader(ctx, command.getHeader(), byteBuf);
        byteBuf.writeBytes(command.getPayload());
    }

    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
