package com.beinglee.rpc.server;

import com.beinglee.hello.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhanglu
 * @date 2020/6/13 14:57
 */

public class HelloServiceImpl implements HelloService {

    private static final Logger log = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        log.info("HelloServiceImpl收到:{}", name);
        String ret = "Hello-" + name;
        log.info("HelloServiceImpl返回:{}", ret);
        return ret;
    }

    @Override
    public int getAge() {
        return 18;
    }
}
