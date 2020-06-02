package com.beinglee.rpc.api;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * @author zhanglu
 * @date 2020/6/2 20:54
 */
public interface NameService {

    /**
     * 支持的协议
     *
     * @return 协议
     */
    Collection<String> supportedSchemes();

    /**
     * 连接注册中心
     *
     * @param nameServiceUri 注册中心地址
     */
    void connect(URI nameServiceUri);

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param uri         服务地址
     * @throws IOException 注册异常
     */
    void registerService(String serviceName, URI uri) throws IOException;


    /**
     * 查询服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     * @throws IOException 查询异常
     */
    URI lookupService(String serviceName) throws IOException;

}
