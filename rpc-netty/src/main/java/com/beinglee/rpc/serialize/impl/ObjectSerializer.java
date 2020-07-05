package com.beinglee.rpc.serialize.impl;

import com.beinglee.rpc.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ObjectSerializer implements Serializer<Object> {

    private static final Logger log = LoggerFactory.getLogger(ObjectSerializer.class);

    @Override
    public int size(Object entry) {
        return toByteArray(entry).length;
    }

    @Override
    public void serialize(Object entry, byte[] bytes, int offset, int length) {
        byte[] objBytes = toByteArray(entry);
        System.arraycopy(objBytes, 0, bytes, offset, objBytes.length);
    }

    @Override
    public Object parse(byte[] bytes, int offset, int length) {
        byte[] objBytes = new byte[length];
        System.arraycopy(bytes, offset, objBytes, 0, length);
        return toObject(objBytes);
    }

    @Override
    public byte type() {
        return Types.TYPE_OBJECT;
    }

    @Override
    public Class<Object> getSerializeClass() {
        return Object.class;
    }

    public byte[] toByteArray(Object obj) {
        byte[] bytes = new byte[0];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            log.error("ObjectSerializer toByteArray occur error!", e);
        }
        return bytes;
    }

    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            log.error("ObjectSerializer toObject occur error!", e);
        }
        return obj;

    }
}
