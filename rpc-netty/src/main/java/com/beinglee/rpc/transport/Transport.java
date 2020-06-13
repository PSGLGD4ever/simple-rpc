package com.beinglee.rpc.transport;

import com.beinglee.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @author zhanglu
 * @date 2020/6/8 10:48
 */
public interface Transport {

    /**
     * 发送请求命令
     *
     * @param request 请求命令
     * @return 返回值是一个Future
     */
    CompletableFuture<Command> send(Command request);

}
