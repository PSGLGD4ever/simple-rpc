package com.beinglee.rpc.server;

import com.beinglee.rpc.client.ServiceTypes;
import com.beinglee.rpc.client.stubs.RpcRequest;
import com.beinglee.rpc.serialize.SerializeSupport;
import com.beinglee.rpc.spi.Singleton;
import com.beinglee.rpc.transport.RequestHandler;
import com.beinglee.rpc.transport.command.Code;
import com.beinglee.rpc.transport.command.Command;
import com.beinglee.rpc.transport.command.Header;
import com.beinglee.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhanglu
 * @date 2020/6/28 16:44
 */

@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {

    private static final Logger log = LoggerFactory.getLogger(RpcRequestHandler.class);

    private Map<String, Object> serviceProviders = new ConcurrentHashMap<>();

    @Override
    public Command handle(Command request) {
        Header header = request.getHeader();
        RpcRequest rpcRequest = SerializeSupport.parse(request.getPayload());
        try {
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                Object[] args = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Class[] paramTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                }
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), paramTypes);
                String result = (String) method.invoke(serviceProvider, args);
                Header responseHeader = new ResponseHeader(type(), header.getVersion(), header.getRequestId());
                return new Command(responseHeader, SerializeSupport.serialize(result));
            }
            log.warn("No service provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No Provider!"), new byte[0]);

        } catch (Throwable t) {
            log.warn("service provider occur exception", t);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }

    @Override
    public <T> void addServiceProvider(T serviceProvider, Class<? extends T> serviceClass) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        log.info("Add service: {},provider: {}", serviceClass.getCanonicalName(), serviceProvider);
    }
}
