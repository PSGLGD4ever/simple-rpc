package com.beinglee.rpc.serialize.impl;

public class ObjArraysSerializer extends ObjectSerializer {

    @Override
    public byte type() {
        return Types.TYPE_OBJECT_ARRAYS;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Class getSerializeClass() {
        Object[] objects = new Object[]{};
        return objects.getClass();
    }

}
