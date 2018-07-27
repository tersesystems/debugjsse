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
    <artifactId>debugjsse</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```


## Running

To set the provider, call `Security.addProvider` and then call setAsDefault().

```java
import com.tersesystems.proxyjsse.DebugJSSEProvider;

DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
        Security.addProvider(debugJSSEProvider);
        debugJSSEProvider.setAsDefault();
```

The debug implementation is `Debug`, but you can customize it is `setDebug`.

```java
public class Main {

    private static final org.slf4j.ext.XLogger logger = XLoggerFactory.getXLogger(Main.class);

    private static final Debug log4jDebug = new Debug() {
        @Override
        public void enter(String message) {
            logger.entry(message);
        }

        @Override
        public void exit(String message, Object result) {
            logger.trace(message);
            logger.exit(result);
        }

        @Override
        public void exception(String message, Exception e) {
            logger.catching(e);
        }
    };

    public static void main(String[] args) throws Exception {
        DebugJSSEProvider debugJSSEProvider = new DebugJSSEProvider();
        Security.addProvider(debugJSSEProvider);
        debugJSSEProvider.setAsDefault();
        DebugJSSEProvider.setDebug(log4jDebug);
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
2018-07-26 20:25:18,977 TRACE [main] Main$1.enter:20 - entry with (getAcceptedIssuers)
2018-07-26 20:25:18,980 TRACE [main] Main$1.exit:25 - getAcceptedIssuers
2018-07-26 20:25:18,980 TRACE [main] Main$1.exit:26 - exit with ([])
trustManager = []
```

##
