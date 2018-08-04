package com.tersesystems.debugjsse;

import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import java.security.Provider;

public class DebugJSSEProviderTest {

    @Test
    public void testProviderImplementation() throws Exception {
        DebugJSSEProvider p = DebugJSSEProvider.enable();


        KeyManagerFactory kmf;
        kmf = KeyManagerFactory.getInstance("debugSunX509");
        same(p, kmf.getProvider());

        kmf = KeyManagerFactory.getInstance("debugNewSunX509");
        same(p, kmf.getProvider());

        kmf = KeyManagerFactory.getInstance("debugPKIX");
        same(p, kmf.getProvider());

        TrustManagerFactory tmf;
        tmf = TrustManagerFactory.getInstance("debugSunX509");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("debugPKIX");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("debugSunPKIX");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("debugX509");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("debugX.509");
        same(p, tmf.getProvider());
    }

    private static void same(Provider p1, Provider p2) throws Exception {
        if (p1 != p2) {
            throw new Exception("not same object");
        }
    }

}
