package com.beinglee.rpc.transport;

import com.beinglee.rpc.transport.command.Command;

/**
 * @author zhanglu
 * @date 2020/6/28 16:16
 */
public interface RequestHandler {

    Command handle(Command request);

    int type();

}
