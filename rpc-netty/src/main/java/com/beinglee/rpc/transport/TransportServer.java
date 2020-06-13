package com.beinglee.rpc.transport;

/**
 * @author zhanglu
 * @date 2020/6/13 16:55
 */
public interface TransportServer {

    void start(RequestHandlerRegistry registry, int port);

    void stop();

}
