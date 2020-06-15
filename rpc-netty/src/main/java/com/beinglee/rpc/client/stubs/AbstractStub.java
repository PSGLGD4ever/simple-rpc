package com.beinglee.rpc.client.stubs;

import com.beinglee.rpc.client.RequestIdSupport;
import com.beinglee.rpc.client.ServiceStub;
import com.beinglee.rpc.client.ServiceTypes;
import com.beinglee.rpc.serialize.SerializeSupport;
import com.beinglee.rpc.transport.Transport;
import com.beinglee.rpc.transport.Version;
import com.beinglee.rpc.transport.command.Code;
import com.beinglee.rpc.transport.command.Command;
import com.beinglee.rpc.transport.command.Header;
import com.beinglee.rpc.transport.command.ResponseHeader;

public abstract class AbstractStub implements ServiceStub {

    private Transport transport;

    protected byte[] invokeRemote(RpcRequest rpcRequest) {
        Header header = new Header(RequestIdSupport.next(), Version.VERSION_1, ServiceTypes.TYPE_RPC_REQUEST);
        byte[] payload = SerializeSupport.serialize(rpcRequest);
        try {
            Command responseCommand = transport.send(new Command(header, payload)).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
