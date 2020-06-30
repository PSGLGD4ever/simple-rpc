import com.beinglee.rpc.RpcAccessPoint;
import com.beinglee.rpc.spi.ServiceSupport;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhanglu
 * @date 2020/6/30 21:29
 */
public class SPITest {

    @Test
    public void rpcAccessPointTest() {
        RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
        Assert.assertNotNull(rpcAccessPoint);
    }
}
