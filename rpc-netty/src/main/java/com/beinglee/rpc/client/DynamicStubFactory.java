package com.beinglee.rpc.client;

import com.beinglee.rpc.transport.Transport;

/**
 * 动态创建代理桩
 *
 * @author zhanglu
 * @date 2020/6/13 18:21
 */
public class DynamicStubFactory implements StubFactory {

    private static final String STUB_SOURCE_TEMPLATE = "";

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        return null;
    }
}
