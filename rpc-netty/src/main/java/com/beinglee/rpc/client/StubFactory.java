package com.beinglee.rpc.client;

import com.beinglee.rpc.transport.Transport;

/**
 * @author zhanglu
 * @date 2020/6/13 17:24
 */
public interface StubFactory {
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
