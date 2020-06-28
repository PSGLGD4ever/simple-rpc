package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Header;
import com.beinglee.rpc.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @author zhanglu
 * @date 2020/6/28 10:26
 */
public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        super.encodeHeader(ctx, header, byteBuf);
        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            byteBuf.writeInt(responseHeader.getErrorLength());
            byteBuf.writeBytes(responseHeader.getError().getBytes(StandardCharsets.UTF_8));
        }
    }
}
