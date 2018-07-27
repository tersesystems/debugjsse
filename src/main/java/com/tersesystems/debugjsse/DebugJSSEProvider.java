package com.tersesystems.debugjsse;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import java.security.Provider;
import java.security.Security;

public class DebugJSSEProvider extends Provider {

    // https://bugs.openjdk.java.net/browse/JDK-8169745, PKIX is the default.
    static final String DEFAULT_KEYMANAGER_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
    static final String DEFAULT_TRUSTMANAGER_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();

    private final static String KEY_MANAGER_ALGORITHM = "debug" + DEFAULT_KEYMANAGER_ALGORITHM;
    private final static String TRUST_MANAGER_ALGORITHM = "debug" + DEFAULT_TRUSTMANAGER_ALGORITHM;

    public final static String NAME = "debugJSSE";
    private final static Double VERSION = 1.0;
    private final static String INFO = "Debug JSSE";

    private static final String SSL_KEY_MANAGER_FACTORY_SECPROP = "ssl.KeyManagerFactory.algorithm";
    private static final String SSL_TRUST_MANAGER_FACTORY_SECPROP = "ssl.TrustManagerFactory.algorithm";
    private static final String KEY_MANAGER_FACTORY = "KeyManagerFactory";
    private static final String TRUST_MANAGER_FACTORY = "TrustManagerFactory";

    private class ProxyKeyManagerFactoryService extends Service {
        public ProxyKeyManagerFactoryService() {
            super(DebugJSSEProvider.this, KEY_MANAGER_FACTORY, KEY_MANAGER_ALGORITHM,
                    DebugKeyManagerFactorySpi.class.getName(), null, null);
        }
    }

    private class ProxyTrustManagerFactoryService extends Service {
        public ProxyTrustManagerFactoryService() {
            super(DebugJSSEProvider.this, TRUST_MANAGER_FACTORY, TRUST_MANAGER_ALGORITHM,
                    DebugTrustManagerFactorySpi.class.getName(), null, null);
        }
    }

    static Debug debug = new SystemOutDebug();

    public DebugJSSEProvider() {
        super(NAME, VERSION, INFO);
        putService(new ProxyKeyManagerFactoryService());
        putService(new ProxyTrustManagerFactoryService());
    }

    public static void disable() {
        if (KEY_MANAGER_ALGORITHM.equals(Security.getProperty(SSL_KEY_MANAGER_FACTORY_SECPROP))) {
            Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, DEFAULT_KEYMANAGER_ALGORITHM);
        }

        if (TRUST_MANAGER_ALGORITHM.equals(Security.getProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP))) {
            Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, DEFAULT_TRUSTMANAGER_ALGORITHM);
        }

        if (Security.getProvider(NAME) != null) {
            Security.removeProvider(NAME);
        }
    }

    public void setAsDefault() {
        Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, KEY_MANAGER_ALGORITHM);
        Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, TRUST_MANAGER_ALGORITHM);
    }

    public void unsetAsDefault() {
        Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, DEFAULT_KEYMANAGER_ALGORITHM);
        Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, DEFAULT_TRUSTMANAGER_ALGORITHM);
    }

    public static void setDebug(Debug newDebug) {
        debug = newDebug;
    }
}
