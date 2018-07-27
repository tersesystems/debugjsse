# Proxy JSSE Provider

This is a JSSE provider that provides logging for entrance and exit of the trust manager, delegating to the original `KeyManager` and `TrustManager`.

This works on all JSSE implementations, but is transparent, and logs before and after every call.

## Installation

The artifacts are on Bintray JCenter:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
    
    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-tersesystems-debugjsseprovider</id>
                    <name>bintray</name>
                    <url>https://dl.bintray.com/tersesystems/debugjsseprovider</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-tersesystems-debugjsseprovider</id>
                    <name>bintray-plugins</name>
                    <url>https://dl.bintray.com/tersesystems/debugjsseprovider</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>
</settings>
```

And the artifact is listed as:

```xml
<dependency>
    <groupId>com.tersesystems.debugjsse</groupId>
    <artifactId>debugjsseprovider</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```


## Running

To set the provider, call `Security.addProvider` and then call setAsDefault().

```java
import com.tersesystems.debugjsse.DebugJSSEProvider;

DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
Security.addProvider(debugJSSEProvider);
debugJSSEProvider.setAsDefault();
```

The debug implementation is `SystemOutDebug`, but you can customize it is `setDebug`.

```java
import com.tersesystems.debugjsse.Debug;
import com.tersesystems.debugjsse.DebugJSSEProvider;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Main {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("Main");

    private static final Debug slf4jDebug = new Debug() {
        @Override
        public void enter(X509ExtendedTrustManager delegate, String method, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
            logger.trace(msg);
        }

        @Override
        public void enter(X509ExtendedKeyManager delegate, String method, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
            logger.trace(msg);
        }

        @Override
        public <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s, result = %s", method, Arrays.toString(args), delegate, result);
            logger.trace(msg);
            return result;
        }

        @Override
        public <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s, result = %s", method, Arrays.toString(args), delegate, result);
            logger.trace(msg);
            return result;
        }

        @Override
        public void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
            logger.error(msg, e);
        }

        @Override
        public void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args) {
            String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
            logger.error(msg, e);
        }
    };

    public static void main(String[] args) throws Exception {
        DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
        Security.addProvider(debugJSSEProvider);
        debugJSSEProvider.setAsDefault();
        DebugJSSEProvider.setDebug(slf4jDebug);
        KeyStore ks = emptyStore();

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(ks);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        X509ExtendedTrustManager trustManager = (X509ExtendedTrustManager) trustManagers[0];

        X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
        System.out.println("trustManager = " + Arrays.toString(acceptedIssuers));
    }

    private static KeyStore emptyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, "changeit".toCharArray());
        return ks;
    }
}
```

Produces the output:

```
2018-07-27 06:30:04,545 TRACE [main] - getAcceptedIssuers: args = [] with delegate = sun.security.ssl.X509TrustManagerImpl@29444d75
2018-07-27 06:30:04,549 TRACE [main] - getAcceptedIssuers: args = [] with delegate = sun.security.ssl.X509TrustManagerImpl@29444d75, result = [Ljava.security.cert.X509Certificate;@7bfcd12c
trustManager = []
```

##
