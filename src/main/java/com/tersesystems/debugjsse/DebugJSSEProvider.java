package com.tersesystems.debugjsse;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class DebugJSSEProvider extends Provider {

    private static String defaultKeyManagerAlgorithm;
    private static String defaultTrustManagerAlgorithm;

    private final static String NAME = "debugJSSE";
    private final static Double VERSION = 1.0;
    private final static String INFO = "Debug JSSE";

    private static final String SSL_KEY_MANAGER_FACTORY_SECPROP = "ssl.KeyManagerFactory.algorithm";
    private static final String SSL_TRUST_MANAGER_FACTORY_SECPROP = "ssl.TrustManagerFactory.algorithm";
    private static final String KEY_MANAGER_FACTORY = "KeyManagerFactory";
    private static final String TRUST_MANAGER_FACTORY = "TrustManagerFactory";

    private static boolean enabled = false;

    private class DebugSunX509KeyManagerFactoryService extends Service {
        public DebugSunX509KeyManagerFactoryService() {
            super(DebugJSSEProvider.this, KEY_MANAGER_FACTORY, "debugSunX509",
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
        public DebugNewSunX509KeyManagerFactoryService() {
            super(DebugJSSEProvider.this, KEY_MANAGER_FACTORY, "debugNewSunX509",
                    DebugNewSunX509KeyManagerFactorySpi.class.getName(), Collections.singletonList("debugPKIX"), null);
        }
    }

    public static final class DebugNewSunX509KeyManagerFactorySpi extends DebugKeyManagerFactorySpi {
        @Override
        public String getAlgorithm() {
            return "NewSunX509";
        }
    }

    private class DebugSunX509TrustManagerFactoryService extends Service {
        public DebugSunX509TrustManagerFactoryService() {
            super(DebugJSSEProvider.this, TRUST_MANAGER_FACTORY, "debugSunX509",
                    DebugSunX509TrustManagerFactorySpi.class.getName(), null, null);
        }
    }

    public static final class DebugSunX509TrustManagerFactorySpi extends DebugTrustManagerFactorySpi {
        public DebugSunX509TrustManagerFactorySpi() throws NoSuchAlgorithmException {
            super();
        }

        @Override
        public String getAlgorithm() {
            return "SunX509";
        }
    }

    private class DebugPKIXTrustManagerFactoryService extends Service {
        public DebugPKIXTrustManagerFactoryService() {
            super(DebugJSSEProvider.this, TRUST_MANAGER_FACTORY,
                    "debugPKIX",
                    DebugPKIXTrustManagerFactorySpi.class.getName(),
                    Arrays.asList("debugSunPKIX", "debugX509", "debugX.509"),
                    null);
        }
    }

    public static final class DebugPKIXTrustManagerFactorySpi extends DebugTrustManagerFactorySpi {
        public DebugPKIXTrustManagerFactorySpi() throws NoSuchAlgorithmException {
            super();
        }

        @Override
        public String getAlgorithm() {
            return "PKIX";
        }
    }


    static Debug debug = new SystemOutDebug();

    public DebugJSSEProvider() {
        super(NAME, VERSION, INFO);

        // https://www.cs.mun.ca/java-api-1.5/guide/security/jce/HowToImplAJCEProvider.html#AlgAliases
        putService(new DebugSunX509KeyManagerFactoryService());
        putService(new DebugNewSunX509KeyManagerFactoryService());
        putService(new DebugSunX509TrustManagerFactoryService());
        putService(new DebugPKIXTrustManagerFactoryService());
    }

    public static DebugJSSEProvider enable() {
        if (enabled == false) {
            defaultKeyManagerAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            defaultTrustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm();

            DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
            Security.addProvider(debugJSSEProvider);
            debugJSSEProvider.setAsDefault();

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
            if (defaultKeyManagerAlgorithm.equals(Security.getProperty(SSL_KEY_MANAGER_FACTORY_SECPROP))) {
                Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, defaultKeyManagerAlgorithm);
            }

            if (defaultTrustManagerAlgorithm.equals(Security.getProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP))) {
                Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, defaultTrustManagerAlgorithm);
            }

            if (Security.getProvider(NAME) != null) {
                Security.removeProvider(NAME);
            }
            enabled = false;
        } else {
            throw new IllegalStateException("Provider is disabled!");
        }
    }

    // https://github.com/cloudfoundry/java-buildpack-security-provider/blob/master/src/main/java/org/cloudfoundry/security/CloudFoundryContainerProvider.java
    public void setAsDefault() {
        Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, "debug" + defaultKeyManagerAlgorithm);
        Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, "debug" + defaultTrustManagerAlgorithm);
    }

    public void unsetAsDefault() {
        Security.setProperty(SSL_KEY_MANAGER_FACTORY_SECPROP, defaultKeyManagerAlgorithm);
        Security.setProperty(SSL_TRUST_MANAGER_FACTORY_SECPROP, defaultTrustManagerAlgorithm);
    }

    public void setDebug(Debug newDebug) {
        debug = newDebug;
    }

}
