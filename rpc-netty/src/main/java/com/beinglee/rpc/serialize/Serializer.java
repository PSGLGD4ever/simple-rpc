package com.beinglee.rpc.serialize;

public interface Serializer<T> {

    /**
     * 计算对象序列化后的长度，主要用来提前申请内存。
     *
     * @param entry 待序列化的对象
     * @return 对象序列化后的长度
     */
    int size(T entry);

    /**
     * 序列化对象，将指定的对象序列化成字节数组
     *
     * @param entry  要序列化的对象
     * @param bytes  序列化后的数组
     * @param offset 数组的偏移量，从数组的这个位置开始写入数据。
     * @param length 对象序列化后的长度
     */
    void serialize(T entry, byte[] bytes, int offset, int length);

    /**
     * 反序列化对象
     *
     * @param bytes  存放序列化数据的字节数组
     * @param offset 数组的偏移量，从数据的这个位置开始反序列化。
     * @param length 对象序列化后的长度
     * @return 反序列化后的对象
     */
    T parse(byte[] bytes, int offset, int length);

    /**
     * 用一个字节标识对象类型，每种类型的数据应该具有不同的类型值
     *
     * @return 对象类型
     */
    byte type();

    /**
     * 返回序列化对象的Class类型
     */
    Class<T> getSerializeClass();

}
