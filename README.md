# DebugJSSE

[ ![Download](https://api.bintray.com/packages/tersesystems/maven/debugjsse/images/download.svg) ](https://bintray.com/tersesystems/maven/debugjsse/_latestVersion)

This is a JSSE provider that provides logging for entrance and exit of the trust manager, delegating to the original `KeyManager` and `TrustManager`.

This works on all JSSE implementations, but is transparent, and logs before and after every call.

More info in the [blog post](https://tersesystems.com/blog/2018/07/27/debug-java-tls-ssl-provider/).

## Installation

Packages are hosted on jCenter:

### Maven

```xml
<repositories>
  <repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.tersesystems.debugjsse</groupId>
    <artifactId>debugjsse</artifactId>
    <version>0.3.0</version><!-- see badge for latest version -->
</dependency>
```

### sbt

```
resolvers += Resolver.jcenterRepo 
libraryDependencies += "com.tersesystems.debugjsse" % "debugjsse" % "0.3.0"
```

## Running

To set the provider, call `Security.addProvider` and then call `setAsDefault()`.

```java
DebugJSSEProvider provider = new DebugJSSEProvider();
Security.addProvider(provider);
provider.setAsDefault();
```

For convenience, you can call the `enable` method which does the same as the above:

```java
DebugJSSEProvider provider = DebugJSSEProvider.enable();
```

You can change the `Debug` instance by calling `setDebug`:

```java
Debug sysErrDebug = new PrintStreamDebug(System.err);
provider.setDebug(sysErrDebug);
```

And you can add your own logging framework by extending `AbstractDebug`:

```java
org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("Main");
Debug slf4jDebug = new AbstractDebug() {
    @Override
    public void enter(String message) {
        logger.debug(message);
    }

    @Override
    public void exit(String message) {
        logger.debug(message);
    }

    @Override
    public void exception(String message, Exception e) {
        logger.error(message, e);
    }
};
```

Full example here:

```java
import com.tersesystems.debugjsse.AbstractDebug;
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

    private static final Debug slf4jDebug = new AbstractDebug() {
        @Override
        public void enter(String message) {
            logger.debug(message);
        }

        @Override
        public void exit(String message) {
            logger.debug(message);
        }

        @Override
        public void exception(String message, Exception e) {
            logger.error(message, e);
        }
    };

    public static void main(String[] args) throws Exception {
        DebugJSSEProvider.enable().setDebug(slf4jDebug);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, "changeit".toCharArray());

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
2018-07-28 09:44:28,467 DEBUG [main] - enter: javax.net.ssl.TrustManagerFactory@27abe2cd.init: args = KeyStore([])
2018-07-28 09:44:28,470 DEBUG [main] - exit:  javax.net.ssl.TrustManagerFactory@27abe2cd.init: args = KeyStore([]) => null
2018-07-28 09:44:28,470 DEBUG [main] - enter: javax.net.ssl.TrustManagerFactory@27abe2cd.getTrustManagers()
2018-07-28 09:44:28,471 DEBUG [main] - exit:  javax.net.ssl.TrustManagerFactory@27abe2cd.getTrustManagers() => [DebugX509ExtendedTrustManager@1151020327(sun.security.ssl.X509TrustManagerImpl@5479e3f)]
2018-07-28 09:44:28,471 DEBUG [main] - enter: sun.security.ssl.X509TrustManagerImpl@5479e3f.getAcceptedIssuers()
2018-07-28 09:44:28,472 DEBUG [main] - exit:  sun.security.ssl.X509TrustManagerImpl@5479e3f.getAcceptedIssuers() => []
trustManager = []
```

## Releasing

```bash
mvn clean compile package deploy
```

## Further Reading

* https://github.com/cloudfoundry/java-buildpack-security-provider/tree/master/src/main/java/org/cloudfoundry/security
* https://github.com/scholzj/AliasKeyManager
* https://tersesystems.com/blog/2014/07/07/play-tls-example-with-client-authentication/
