package com.tersesystems.debugjsse;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;

public class DebugJSSEProvider extends Provider {

    public final static String NAME = "debugJSSE";
    public final static Double VERSION = 1.0;
    public final static String INFO = "Debug JSSE";

    private static final String KEY_MANAGER_FACTORY = "KeyManagerFactory";
    private static final String TRUST_MANAGER_FACTORY = "TrustManagerFactory";

    private static boolean enabled = false;

    private class DebugSunX509KeyManagerFactoryService extends Service {
        DebugSunX509KeyManagerFactoryService() {
            super(DebugJSSEProvider.this, KEY_MANAGER_FACTORY, "SunX509",
                    DebugSunX509KeyManagerFactorySpi.class.getName(), null, null);
        }
    }

    public static final class DebugSunX509KeyManagerFactorySpi extends DebugKeyManagerFactorySpi {
        @Override
        public String getAlgorithm() {
            return "SunX509";
        }
    }

    private class DebugNewSunX509KeyManagerFactoryService extends Service {
        DebugNewSunX509KeyManagerFactoryService() {
            super(DebugJSSEProvider.this, KEY_MANAGER_FACTORY, "NewSunX509",
                    DebugNewSunX509KeyManagerFactorySpi.class.getName(), Collections.singletonList("PKIX"), null);
        }
    }

    public static final class DebugNewSunX509KeyManagerFactorySpi extends DebugKeyManagerFactorySpi {
        @Override
        public String getAlgorithm() {
            return "NewSunX509";
        }
    }

    private class DebugSunX509TrustManagerFactoryService extends Service {
        DebugSunX509TrustManagerFactoryService() {
            super(DebugJSSEProvider.this, TRUST_MANAGER_FACTORY, "SunX509",
                    DebugSunX509TrustManagerFactorySpi.class.getName(), null, null);
        }
    }

    public static final class DebugSunX509TrustManagerFactorySpi extends DebugTrustManagerFactorySpi {
        public DebugSunX509TrustManagerFactorySpi() throws NoSuchAlgorithmException, NoSuchProviderException {
            super();
        }

        @Override
        public String getAlgorithm() {
            return "SunX509";
        }
    }

    private class DebugPKIXTrustManagerFactoryService extends Service {
        DebugPKIXTrustManagerFactoryService() {
            super(DebugJSSEProvider.this, TRUST_MANAGER_FACTORY,
                    "PKIX",
                    DebugPKIXTrustManagerFactorySpi.class.getName(),
                    Arrays.asList("SunPKIX", "X509", "X.509"),
                    null);
        }
    }

    public static final class DebugPKIXTrustManagerFactorySpi extends DebugTrustManagerFactorySpi {
        public DebugPKIXTrustManagerFactorySpi() throws NoSuchAlgorithmException, NoSuchProviderException {
            super();
        }

        @Override
        public String getAlgorithm() {
            return "PKIX";
        }
    }

    public static Debug debug = new SystemOutDebug();

    public DebugJSSEProvider() {
        super(NAME, VERSION, INFO);

        // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/HowToImplAProvider.html#ProviderService
        // https://www.cs.mun.ca/java-api-1.5/guide/security/jce/HowToImplAJCEProvider.html#AlgAliases
        putService(new DebugSunX509KeyManagerFactoryService());
        putService(new DebugNewSunX509KeyManagerFactoryService());
        putService(new DebugSunX509TrustManagerFactoryService());
        putService(new DebugPKIXTrustManagerFactoryService());

        put("SSLContext.TLSv1",
                "com.tersesystems.debugjsse.DebugSSLContextSpi$TLS10Context");
        put("SSLContext.TLSv1.1",
                "com.tersesystems.debugjsse.DebugSSLContextSpi$TLS11Context");
        put("SSLContext.TLSv1.2",
                "com.tersesystems.debugjsse.DebugSSLContextSpi$TLS12Context");
        put("SSLContext.TLS",
                "com.tersesystems.debugjsse.DebugSSLContextSpi$TLSContext");

        put("SSLContext.Default",
                "com.tersesystems.debugjsse.DebugSSLContextSpi$DefaultSSLContext");
    }

    public static DebugJSSEProvider enable() {
        if (!enabled) {
            DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
            Security.insertProviderAt(debugJSSEProvider, 1); // WE ARE MOST IMPORTANTEST!!!111oneone

            enabled = true;
            return debugJSSEProvider;
        } else {
            throw new IllegalStateException("Provider is enabled!");
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void disable() {
        if (enabled) {
            Security.removeProvider(NAME);
            enabled = false;
        } else {
            throw new IllegalStateException("Provider is disabled!");
        }
    }

    public void setDebug(Debug newDebug) {
        debug = newDebug;
    }

}
