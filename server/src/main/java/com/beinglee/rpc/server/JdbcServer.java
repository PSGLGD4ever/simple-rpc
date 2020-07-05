package com.beinglee.rpc.server;

import com.beinglee.hello.HelloService;
import com.beinglee.rpc.NameService;
import com.beinglee.rpc.RpcAccessPoint;
import com.beinglee.rpc.spi.ServiceSupport;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

public class JdbcServer {

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        URI nameServiceUri = URI.create("jdbc:mysql://localhost:3306/nameservice");
        HelloService helloService = new HelloServiceImpl();
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            Closeable ignored = rpcAccessPoint.startServer();
            NameService nameService = rpcAccessPoint.getNameService(nameServiceUri);
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            nameService.registerService(serviceName, uri);
            int ignore = System.in.read();
            System.out.println("Bye");
        }
    }
}
