package com.beinglee.rpc.transport.command;

import java.nio.charset.StandardCharsets;

/**
 * @author zhanglu
 * @date 2020/6/8 11:51
 */
public class ResponseHeader extends Header {

    private int code;
    private String error;

    public ResponseHeader(int type, int version, int requestId, int code, String error) {
        super(type, version, requestId);
        this.code = code;
        this.error = error;
    }

    public ResponseHeader(int type, int version, int requestId) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader(int type, int version, int requestId, Throwable throwable) {
        this(type, version, requestId, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    @Override
    public int length() {
        return super.length() + Integer.BYTES + Command.LENGTH_FIELD_LENGTH +
                (this.error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getErrorLength() {
        return length() - super.length() - Integer.BYTES - Command.LENGTH_FIELD_LENGTH;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
