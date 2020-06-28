package com.beinglee.rpc.transport.command;

/**
 * @author zhanglu
 * @date 2020/6/8 11:11
 */

public class Command {

    public static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    private Header header;

    private byte[] payload;

    public Command(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
