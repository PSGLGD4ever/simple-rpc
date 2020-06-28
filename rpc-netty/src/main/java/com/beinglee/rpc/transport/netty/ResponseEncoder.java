package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Header;
import com.beinglee.rpc.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @author zhanglu
 * @date 2020/6/28 10:26
 */
@ChannelHandler.Sharable
public class ResponseEncoder extends CommandEncoder {

    private static ResponseEncoder instance = null;

    private ResponseEncoder() {
    }

    public static ResponseEncoder getInstance() {
        if (instance == null) {
            instance = new ResponseEncoder();
        }
        return instance;
    }


    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) {
        super.encodeHeader(ctx, header, byteBuf);
        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            byteBuf.writeInt(responseHeader.getErrorLength());
            byte[] error = new byte[0];
            if (responseHeader.getError() != null) {
                error = responseHeader.getError().getBytes(StandardCharsets.UTF_8);
            }
            byteBuf.writeBytes(error);
        }
    }
}
