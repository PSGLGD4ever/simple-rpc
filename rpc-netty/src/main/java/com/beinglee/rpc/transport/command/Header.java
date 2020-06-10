package com.beinglee.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanglu
 * @date 2020/6/8 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    private int requestId;
    private int version;
    private int type;

    public int length() {
        return Integer.BYTES * 3;
    }

}
