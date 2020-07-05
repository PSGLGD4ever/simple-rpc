package com.beinglee.rpc.client;

import com.beinglee.rpc.transport.Transport;

public class CGLIBStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        CGLibDynamicProxy cgLibDynamicProxy = new CGLibDynamicProxy(serviceClass);
        cgLibDynamicProxy.setTransport(transport);
        return (T) cgLibDynamicProxy.getProxyInstance();
    }
}
