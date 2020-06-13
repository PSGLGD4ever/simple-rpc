package com.beinglee.rpc.server;

import com.beinglee.hello.HelloService;
import com.beinglee.rpc.NameService;
import com.beinglee.rpc.RpcAccessPoint;
import com.beinglee.rpc.spi.ServiceSupport;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

/**
 * 主要用来注册实现Api接口的服务
 *
 * @author zhanglu
 * @date 2020/6/13 15:08
 */
public class Server {

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        File temDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(temDirFile, "simple_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            Closeable ignored = rpcAccessPoint.startServer();
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            nameService.registerService(serviceName, uri);
        }
    }

}
