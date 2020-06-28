package com.beinglee.rpc.client;

import com.beinglee.rpc.transport.Transport;
import com.itranswarp.compiler.JavaStringCompiler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 动态创建代理桩
 *
 * @author zhanglu
 * @date 2020/6/13 18:21
 */
@Slf4j
public class DynamicStubFactory implements StubFactory {

    private static final String STUB_SOURCE_TEMPLATE = "package com.beinglee.rpc.client.stubs;\n" +
            "import com.beinglee.rpc.serialize.SerializeSupport;\n" +
            "\n" +
            "public class %s extends AbstractStub implements %s { \n" +
            "@Override\n" +
            "public String %s(String arg) {\n" +
            "String interfaceName = %s;\n" +
            "String methodName = %s;\n" +
            "byte[] serializedArguments = SerializeSupport.serialize(arg);\n" +
            "RpcRequest request = new RpcRequest(interfaceName, methodName, serializedArguments);\n" +
            "return SerializeSupport.parse(invokeRemote(request));\n" +
            "}";

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String stubFullName = "com.beinglee.rpc.client.stubs." + stubSimpleName;
            String interfaceClassName = serviceClass.getCanonicalName();
            String methodName = serviceClass.getMethods()[0].getName();
            String stubSource = String.format(STUB_SOURCE_TEMPLATE, stubFullName, interfaceClassName, methodName, interfaceClassName, methodName);
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", stubSource);
            Class<?> stubClass = compiler.loadClass(stubFullName, results);
            ServiceStub stubInstance = (ServiceStub) stubClass.newInstance();
            stubInstance.setTransport(transport);
            return (T) stubInstance;
        } catch (Throwable t) {
            log.error("java dynamic load stub class occur error!", t);
            throw new RuntimeException(t);
        }
    }
}
