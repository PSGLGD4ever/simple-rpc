package com.beinglee.rpc.client;

import com.beinglee.rpc.transport.Transport;

/**
 * 动态创建代理桩
 * <p>
 * jdk的动态代理只能代理实现接口的实现类，cglib虽然可以代理非接口实现类。
 * 但是与这里不同的是我们要动态生成一个实现类，而不是代理已经存在的实现类。
 * 我们这里通过一份桩的模板代码，生成桩的源码，然后动态编译，加载这个桩。
 * <p/>
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
