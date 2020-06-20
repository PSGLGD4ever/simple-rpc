package com.beinglee.rpc.transport.netty;

import com.beinglee.rpc.transport.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author zhanglu
 * @date 2020/6/20 16:04
 */
@ChannelHandler.Sharable
public class CommandCodecHandler extends MessageToMessageCodec<Byte[], Command> {

    private static CommandCodecHandler instance = null;

    private CommandCodecHandler() {
    }

    public static CommandCodecHandler getInstance() {
        if (instance == null) {
            instance = new CommandCodecHandler();
        }
        return instance;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Command msg, List<Object> out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Byte[] msg, List<Object> out) throws Exception {

    }
}
