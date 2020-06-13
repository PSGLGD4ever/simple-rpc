package com.beinglee.rpc.client.stubs;

import com.beinglee.rpc.client.StubFactory;
import com.beinglee.rpc.transport.Transport;

/**
 * 动态创建代理桩
 *
 * @author zhanglu
 * @date 2020/6/13 18:21
 */
public class DynamicStubFactory implements StubFactory {

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        return null;
    }
}
