package com.beinglee.rpc.server;

/**
 * @author zhanglu
 * @date 2020/6/13 17:37
 */
public interface ServiceProviderRegistry {

    <T> void addServiceProvider(T serviceProvider, Class<? extends T> serviceClass);
}
