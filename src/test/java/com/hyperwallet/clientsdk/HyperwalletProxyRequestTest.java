package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.util.HyperwalletApiClient;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author kfan 2020-07-10 23:07
 */
public class HyperwalletProxyRequestTest {


    @Test
    public void testProxy() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 3128));
        HyperwalletApiClient  hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0"
                , proxy);

        String str = hyperwalletApiClient.get("http://www.baidu.com",String.class);
        System.err.println(str);
    }
}
