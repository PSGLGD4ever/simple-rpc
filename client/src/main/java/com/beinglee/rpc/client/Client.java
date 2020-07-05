package com.beinglee.rpc.client;

import com.beinglee.hello.HelloService;
import com.beinglee.rpc.NameService;
import com.beinglee.rpc.RpcAccessPoint;
import com.beinglee.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

/**
 * @author zhanglu
 * @date 2020/6/13 15:39
 */

public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "beinglee_simple_rpc_name_service.data");
        String name = "beingLee";
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            URI uri = nameService.lookupService(serviceName);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            String ret = helloService.hello(name);
            log.info("收到服务端响应------>" + ret);
        }
    }
}
