package com.beinglee.rpc.client.stubs;

import com.beinglee.hello.HelloService;
import com.beinglee.rpc.serialize.SerializeSupport;

public class HelloServiceStub extends AbstractStub implements HelloService {

    @Override
    public String hello(String name) {

        return SerializeSupport.parse(invokeRemote(null));
    }
}
