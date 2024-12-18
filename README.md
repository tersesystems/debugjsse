# DebugJSSE

[ ![Download](https://api.bintray.com/packages/tersesystems/maven/debugjsse/images/download.svg) ](https://bintray.com/tersesystems/maven/debugjsse/_latestVersion)

This is a JSSE provider that provides logging for entrance and exit of the trust manager, delegating to the original `KeyManager` and `TrustManager`.

This works on all JSSE implementations, but is transparent, and logs before and after every call.

This only works on "SunJSSE" right now.

More info in the [blog post](https://tersesystems.com/blog/2018/07/27/debug-java-tls-ssl-provider/).

## Installation

```
<dependency>
    <groupId>com.tersesystems.debugjsse</groupId>
    <artifactId>debugjsse</artifactId>
    <version>1.0.0</version>
</dependency>
```

### sbt

```
libraryDependencies += "com.tersesystems.debugjsse" % "debugjsse" % "1.0.0"
```

## Installing Provider

The security provider must be installed before it will work.

### Installing Dynamically

Calling `enable` will set the debug provider at the highest level, so it preempts "SunJSSE" and then delegates to it.

```java
DebugJSSEProvider provider = DebugJSSEProvider.enable();
```

### Installing Statically

You can install the [provider statically](https://docs.oracle.com/javase/9/security/java-secure-socket-extension-jsse-reference-guide.htm#JSSEC-GUID-8BC473B2-CD64-4E8B-8136-80BB286091B1) by adding the provider as the highest priority item in `<java-home>/conf/security/java.security`:

```bash
security.provider.1=debugJSSE|com.tersesystems.debugjsse.DebugJSSEProvider
```

## Setting Debug

You can change the `Debug` instance by calling `setDebug`:

```java
Debug sysErrDebug = new PrintStreamDebug(System.err);
provider.setDebug(sysErrDebug);
```

or use java.util.logging:

```java
provider.setDebug(new LoggingDebug(DEBUG, debugLogger));
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

## Full Example

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

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        SSLEngine sslEngine = sslContext.createSSLEngine();

        System.out.println("sslEngine = " + sslEngine);
    }
}
```

Produces the output:

```
2018-08-05 11:47:15,108 DEBUG [main] - enter: javax.net.ssl.SSLContext@50cbc42f.init(keyManagers = null, trustManagers = null, secureRandom = null)
2018-08-05 11:47:15,111 DEBUG [main] - enter: trustManagerFactory1-1533494835111@1265210847.init: args = null
2018-08-05 11:47:15,184 DEBUG [main] - exit:  trustManagerFactory1-1533494835111@1265210847.init: args = null => null
2018-08-05 11:47:15,184 DEBUG [main] - enter: trustManagerFactory1-1533494835111@1265210847.getTrustManagers()
2018-08-05 11:47:15,185 DEBUG [main] - exit:  trustManagerFactory1-1533494835111@1265210847.getTrustManagers() => [trustManager1-1533494835185@627185331]
2018-08-05 11:47:15,186 DEBUG [main] - exit:  javax.net.ssl.SSLContext@50cbc42f.init(keyManagers = null, trustManagers = null, secureRandom = null) => null
2018-08-05 11:47:15,186 DEBUG [main] - enter: javax.net.ssl.SSLContext@50cbc42f.createSSLEngine()
2018-08-05 11:47:15,190 DEBUG [main] - exit:  javax.net.ssl.SSLContext@50cbc42f.createSSLEngine() => 2a18f23c[SSLEngine[hostname=null port=-1] SSL_NULL_WITH_NULL_NULL]
sslEngine = 2a18f23c[SSLEngine[hostname=null port=-1] SSL_NULL_WITH_NULL_NULL]
```

## Further Reading

* https://github.com/cloudfoundry/java-buildpack-security-provider/tree/master/src/main/java/org/cloudfoundry/security
* https://github.com/scholzj/AliasKeyManager
* https://tersesystems.com/blog/2014/07/07/play-tls-example-with-client-authentication/
