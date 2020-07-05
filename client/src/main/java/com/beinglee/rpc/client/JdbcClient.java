package com.beinglee.rpc.client;

import com.beinglee.hello.HelloService;
import com.beinglee.rpc.NameService;
import com.beinglee.rpc.RpcAccessPoint;
import com.beinglee.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class JdbcClient {

    private static final Logger log = LoggerFactory.getLogger(JdbcClient.class);

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        URI nameServiceUri = URI.create("jdbc:mysql://localhost:3306/nameservice");
        String name = "beingLee";
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService(nameServiceUri);
            URI uri = nameService.lookupService(serviceName);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            String ret = helloService.hello(name);
            log.info("收到服务端响应------>" + ret);
            int age = helloService.getAge();
            log.info("收到服务端响应------>" + age);
        }
    }
}
