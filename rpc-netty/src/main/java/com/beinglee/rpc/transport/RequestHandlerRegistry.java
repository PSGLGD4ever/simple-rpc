package com.beinglee.rpc.transport;

import com.beinglee.rpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglu
 * @date 2020/6/13 17:51
 */
@Slf4j
public class RequestHandlerRegistry {

    private static RequestHandlerRegistry instance = null;

    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();

    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
        for (RequestHandler handler : requestHandlers) {
            handlerMap.put(handler.type(), handler);
            log.info("Load request handler, type: {}, class: {}.", handler.type(), handler.getClass().getCanonicalName());
        }
    }

    public static RequestHandlerRegistry getInstance() {
        if (instance == null) {
            instance = new RequestHandlerRegistry();
        }
        return instance;
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }


}
