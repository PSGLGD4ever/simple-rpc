package com.beinglee.rpc.client;

import com.beinglee.rpc.client.stubs.AbstractStub;
import com.beinglee.rpc.client.stubs.RpcRequest;
import com.beinglee.rpc.serialize.SerializeSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibDynamicProxy extends AbstractStub implements MethodInterceptor {

    private final Class target;

    public CGLibDynamicProxy(Class clazz) {
        this.target = clazz;
    }

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        byte[] args = SerializeSupport.serialize(objects);
        RpcRequest rpcRequest = new RpcRequest(target.getCanonicalName(), method.getName(), args);
        byte[] bytes = invokeRemote(rpcRequest);
        return SerializeSupport.parse(bytes);
    }
}
