package com.beinglee.rpc.transport;

import com.beinglee.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @author zhanglu
 * @date 2020/6/11 20:11
 */
public class ResponseFuture {

    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
