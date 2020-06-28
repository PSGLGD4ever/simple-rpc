package com.beinglee.rpc.transport.command;

/**
 * @author zhanglu
 * @date 2020/6/8 11:30
 */

public class Header {

    private int type;
    private int version;
    private int requestId;

    public int length() {
        return Integer.BYTES * 3;
    }

    public Header(int type, int version, int requestId) {
        this.type = type;
        this.version = version;
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getVersion() {
        return version;
    }

    public int getType() {
        return type;
    }
}
