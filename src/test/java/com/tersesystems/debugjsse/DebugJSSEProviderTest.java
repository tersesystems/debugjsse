package com.tersesystems.debugjsse;

import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.security.Provider;

public class DebugJSSEProviderTest {

    @Test
    public void testProviderImplementation() throws Exception {
        DebugJSSEProvider p = DebugJSSEProvider.enable();


        KeyManagerFactory kmf;
        kmf = KeyManagerFactory.getInstance("SunX509");
        same(p, kmf.getProvider());

        kmf = KeyManagerFactory.getInstance("NewSunX509");
        same(p, kmf.getProvider());

        kmf = KeyManagerFactory.getInstance("PKIX");
        same(p, kmf.getProvider());

        TrustManagerFactory tmf;
        tmf = TrustManagerFactory.getInstance("SunX509");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("PKIX");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("SunPKIX");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("X509");
        same(p, tmf.getProvider());

        tmf = TrustManagerFactory.getInstance("X.509");
        same(p, tmf.getProvider());

        SSLContext ctx = SSLContext.getInstance("TLSv1");
        same(p, ctx.getProvider());

        ctx = SSLContext.getInstance("TLSv1");
        same(p, ctx.getProvider());

        ctx = SSLContext.getInstance("TLSv1.1");
        same(p, ctx.getProvider());

        ctx = SSLContext.getInstance("TLSv1.2");
        same(p, ctx.getProvider());

        ctx = SSLContext.getInstance("TLS");
        same(p, ctx.getProvider());

        ctx = SSLContext.getDefault();
        same(p, ctx.getProvider());
    }

    private static void same(Provider p1, Provider p2) throws Exception {
        if (p1 != p2) {
            throw new Exception("not same object");
        }
    }

}
