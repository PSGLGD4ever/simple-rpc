package com.beinglee.rpc.serialize.impl;

import com.beinglee.rpc.nameservice.Metadata;
import com.beinglee.rpc.serialize.Serializer;

public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return 0;
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {

    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        return null;
    }

    @Override
    public byte type() {
        return 0;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return null;
    }
}
