package com.beinglee.rpc.server;

import com.beinglee.hello.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglu
 * @date 2020/6/13 14:57
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        log.info("HelloServiceImpl收到:{}", name);
        String ret = "Hello-" + name;
        log.info("HelloServiceImpl返回:{}", ret);
        return ret;
    }
}
