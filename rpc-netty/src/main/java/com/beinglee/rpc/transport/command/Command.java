package com.beinglee.rpc.transport.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanglu
 * @date 2020/6/8 11:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private Header header;

    private byte[] payload;

}
