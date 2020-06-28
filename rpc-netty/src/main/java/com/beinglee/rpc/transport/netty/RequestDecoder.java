package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author zhanglu
 * @date 2020/6/28 9:49
 */
@ChannelHandler.Sharable
public class RequestDecoder extends CommandDecoder {
    private static RequestDecoder instance = null;

    private RequestDecoder() {
    }

    public static RequestDecoder getInstance() {
        if (instance == null) {
            instance = new RequestDecoder();
        }
        return instance;
    }


    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int reqId = byteBuf.readInt();
        return new Header(type, version, reqId);
    }
}
