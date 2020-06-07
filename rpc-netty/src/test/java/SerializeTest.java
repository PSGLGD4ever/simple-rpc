
import com.beinglee.rpc.client.stubs.RpcRequest;
import com.beinglee.rpc.nameservice.Metadata;
import com.beinglee.rpc.serialize.SerializeSupport;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SerializeTest {

    @Test
    public void rpcRequestSerialize() {
        String interfaceName = "com.being.hello";
        String methodName = "test";
        String arguments = "name";
        byte[] serializedArguments = arguments.getBytes(StandardCharsets.UTF_8);
        byte[] serialize = SerializeSupport.serialize(new RpcRequest(interfaceName, methodName, serializedArguments));
        RpcRequest parse = SerializeSupport.parse(serialize);
        Assert.assertEquals(parse.getInterfaceName(), interfaceName);
        Assert.assertEquals(parse.getMethodName(), methodName);
        Assert.assertArrayEquals(parse.getSerializedArguments(), serializedArguments);
    }

    @Test
    public void StringSerialize() {
        String str = "Hello你好周杰伦";
        byte[] serialize = SerializeSupport.serialize(str);
        String parse = SerializeSupport.parse(serialize);
        Assert.assertEquals(str, parse);
    }

    @Test
    public void MetaDataTest() {
        Metadata metadata = new Metadata();
        String key = "Hello";
        List<URI> uriList = new ArrayList<>(2);
        URI uri = URI.create("www.baidu.com");
        URI uri1 = URI.create("www.google.com");
        uriList.add(uri);
        uriList.add(uri1);
        metadata.put(key, uriList);
        byte[] serialize = SerializeSupport.serialize(metadata);
        Metadata parse = SerializeSupport.parse(serialize);
        Assert.assertEquals(parse.size(), metadata.size());
        Assert.assertEquals(parse.get(key), uriList);
        Assert.assertEquals(parse.get(key).get(0), uri);
        Assert.assertEquals(parse.get(key).get(1), uri1);
    }
}
