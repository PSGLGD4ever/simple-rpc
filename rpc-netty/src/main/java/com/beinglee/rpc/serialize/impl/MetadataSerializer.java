package com.beinglee.rpc.serialize.impl;

import com.beinglee.rpc.nameservice.Metadata;
import com.beinglee.rpc.serialize.Serializer;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return Short.BYTES + entry.entrySet().stream().mapToInt(this::entrySize).sum();
    }

    @Override
    public void serialize(Metadata metadata, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putShort(this.toShortSafely(metadata.size()));
        metadata.forEach((k, v) -> {
            buffer.putShort(this.toShortSafely(k.getBytes(StandardCharsets.UTF_8).length));
            buffer.put(k.getBytes(StandardCharsets.UTF_8));
            buffer.putShort(this.toShortSafely(v.size()));
            v.forEach(
                    uri -> {
                        buffer.putShort(this.toShortSafely(uri.toASCIIString().getBytes(StandardCharsets.UTF_8).length));
                        buffer.put(uri.toASCIIString().getBytes(StandardCharsets.UTF_8));
                    }
            );
        });
    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        Metadata metadata = new Metadata();
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int mapSize = buffer.getShort();
        for (int i = 0; i < mapSize; i++) {
            int keyLen = buffer.getShort();
            byte[] keyBytes = new byte[keyLen];
            buffer.get(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);

            int uriListSize = buffer.getShort();
            List<URI> uris = new ArrayList<>(uriListSize);
            for (int k = 0; k < uriListSize; k++) {
                int uriLen = buffer.getShort();
                byte[] uriBytes = new byte[uriLen];
                buffer.get(uriBytes);
                URI uri = URI.create(new String(uriBytes, StandardCharsets.UTF_8));
                uris.add(uri);
            }
            metadata.put(key, uris);
        }
        return metadata;
    }

    @Override
    public byte type() {
        return Types.TYPE_META_DATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }

    private int entrySize(Map.Entry<String, List<URI>> entries) {
        return Short.BYTES
                + entries.getKey().getBytes(StandardCharsets.UTF_8).length
                + Short.BYTES
                + entries.getValue().stream().mapToInt(uri -> Short.BYTES + uri.toASCIIString().getBytes(StandardCharsets.UTF_8).length).sum();
    }

    private short toShortSafely(int v) {
        assert v < Short.MAX_VALUE;
        return (short) v;
    }
}
