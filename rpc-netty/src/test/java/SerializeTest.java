
import com.beinglee.rpc.client.stubs.RpcRequest;
import com.beinglee.rpc.serialize.SerializeSupport;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

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
}
