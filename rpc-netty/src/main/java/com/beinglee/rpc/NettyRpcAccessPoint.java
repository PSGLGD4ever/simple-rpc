package com.beinglee.rpc;

import com.beinglee.rpc.client.StubFactory;
import com.beinglee.rpc.server.ServiceProviderRegistry;
import com.beinglee.rpc.spi.ServiceSupport;
import com.beinglee.rpc.transport.RequestHandlerRegistry;
import com.beinglee.rpc.transport.Transport;
import com.beinglee.rpc.transport.TransportClient;
import com.beinglee.rpc.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanglu
 * @date 2020/6/13 14:39
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {

    private final int port;
    private final TransportClient client;
    private TransportServer server = null;
    private final Map<URI, Transport> clientMap;
    private final StubFactory stubFactory;
    private final URI uri;
    private final ServiceProviderRegistry serviceProviderRegistry;

    public NettyRpcAccessPoint() {
        String host = "localhost";
        this.port = 8080;
        this.uri = URI.create("rpc://" + host + ":" + port);
        this.client = ServiceSupport.load(TransportClient.class);
        this.stubFactory = ServiceSupport.load(StubFactory.class);
        this.serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);
        this.clientMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 3_000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(service, serviceClass);
        return uri;
    }

    @Override
    public Closeable startServer() throws Exception {
        if (this.server == null) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if (server != null) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (server != null) {
            server.stop();
        }
        client.close();
    }

}
