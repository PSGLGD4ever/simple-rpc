package com.beinglee.rpc.transport;

/**
 * @author zhanglu
 * @date 2020/6/13 17:51
 */
public class RequestHandlerRegistry {

    private static RequestHandlerRegistry instance = null;

    private RequestHandlerRegistry() {
    }


    public static RequestHandlerRegistry getInstance() {
        if (instance == null) {
            instance = new RequestHandlerRegistry();
        }
        return instance;
    }

}
