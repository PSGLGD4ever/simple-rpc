package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Header;
import com.beinglee.rpc.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhanglu
 * @date 2020/6/28 11:45
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int reqId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(type, version, reqId, code, error);
    }
}
