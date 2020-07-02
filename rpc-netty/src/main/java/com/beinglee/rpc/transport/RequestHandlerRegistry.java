package com.beinglee.rpc.transport;

import com.beinglee.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglu
 * @date 2020/6/13 17:51
 */

public class RequestHandlerRegistry {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerRegistry.class);

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
