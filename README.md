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
    <version>0.2.0</version><!-- see badge for latest version -->
</dependency>
```

### sbt

```
//resolvers += Resolver.bintrayRepo("tersesystems", "maven")
resolvers += Resolver.jcenterRepo 
libraryDependencies += "com.tersesystems.debugjsse" % "debugjsse" % "0.2.0"
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
2018-07-28 09:33:59,972 DEBUG [main] - enter: javax.net.ssl.TrustManagerFactory@4f4a7090.init: args = KeyStore([mycert=[CN=My Application, O=My Organisation, L=My City, C=DE]])
2018-07-28 09:33:59,976 DEBUG [main] - exit:  javax.net.ssl.TrustManagerFactory@4f4a7090.init: args = KeyStore([mycert=[CN=My Application, O=My Organisation, L=My City, C=DE]]) => null
2018-07-28 09:33:59,976 DEBUG [main] - enter: javax.net.ssl.TrustManagerFactory@4f4a7090.getTrustManagers()
2018-07-28 09:33:59,977 DEBUG [main] - exit:  javax.net.ssl.TrustManagerFactory@4f4a7090.getTrustManagers() => [DebugX509ExtendedTrustManager@1983747920(sun.security.ssl.X509TrustManagerImpl@5c0369c4)]
2018-07-28 09:33:59,977 DEBUG [main] - enter: sun.security.ssl.X509TrustManagerImpl@5c0369c4.getAcceptedIssuers()
2018-07-28 09:33:59,977 DEBUG [main] - exit:  sun.security.ssl.X509TrustManagerImpl@5c0369c4.getAcceptedIssuers() => [CN=My Application, O=My Organisation, L=My City, C=DE]
trustManager = [[
[
  Version: V3
  Subject: CN=My Application, O=My Organisation, L=My City, C=DE
  Signature Algorithm: SHA256withRSA, OID = 1.2.840.113549.1.1.11

  Key:  Sun RSA public key, 2048 bits
  modulus: 18445684969620169238663038449317374250043853243898884437825048111109451569618935930983060115395387844808671898433899434124025436601235980670185525745474534158763371420967126395603073093152289312512559775785785997554951427602976778558877917856982344100439000145295084152454738192502794286827886284827450156420340231349290298971963083058456444330213818560603655914435929477040685871726250318345332250362948918797528301882122584531753800810807810477486090088533932126970148186247239981485310840792267985886607597037498769879661359388177850749834994537268417610162060443895886196006534145541913544701358290419822314761743
  public exponent: 65537
  Validity: [From: Sat Jul 28 09:33:59 PDT 2018,
               To: Sun Jul 28 09:33:59 PDT 2019]
  Issuer: CN=My Application, O=My Organisation, L=My City, C=DE
  SerialNumber: [    4aa3597b]

]
  Algorithm: [SHA256withRSA]
  Signature:
0000: 5B 0C 69 40 00 52 02 F4   E4 EE 3A 51 08 57 CE E2  [.i@.R....:Q.W..
0010: 3F 95 2C A8 A1 D3 E2 43   9B BA C2 DB 80 BC A1 C1  ?.,....C........
0020: A7 10 48 17 EC 0A 2D 0A   17 6E F7 9B 70 62 27 4B  ..H...-..n..pb'K
0030: A9 42 D8 95 7B F8 75 CA   AF 1E 7E 01 5B 0D 81 08  .B....u.....[...
0040: BD 17 D1 00 3F A4 EE B0   D0 50 E9 E5 8C 92 F5 8D  ....?....P......
0050: 64 5D D0 02 58 A7 31 9B   FC EA 06 B9 E7 00 6A 2B  d]..X.1.......j+
0060: CC C0 65 2F 88 64 99 41   A2 95 C5 CE BD 7D 0F A4  ..e/.d.A........
0070: 2F 7D F7 06 81 7A E1 AD   5A 79 5B 50 37 20 69 C5  /....z..Zy[P7 i.
0080: 3D 81 98 F5 60 7C E1 EE   EF 73 25 01 9C 05 B8 53  =...`....s%....S
0090: E9 6B DC 82 70 0E 1D 02   86 34 A4 2E 12 6D 88 2B  .k..p....4...m.+
00A0: C3 F7 0D 71 77 CA A6 51   9E B6 25 16 7B F2 09 03  ...qw..Q..%.....
00B0: 7E B6 5A 68 AD 9C 25 64   2A 65 B3 B7 28 06 53 CA  ..Zh..%d*e..(.S.
00C0: 21 B8 25 0A 91 D6 E1 7A   47 3E 19 FB 2E 74 7B CD  !.%....zG>...t..
00D0: 0B 59 31 04 19 B1 8D 0E   5E 0B 7B CD B9 87 08 83  .Y1.....^.......
00E0: 56 78 E8 1C A2 8C 5D 53   BD E5 05 79 B7 80 B2 16  Vx....]S...y....
00F0: A1 F8 E2 C8 33 35 44 A3   D4 72 64 F5 45 A2 CB 2C  ....35D..rd.E..,

]]
```

## Releasing

```bash
mvn clean compile package deploy
```

## Further Reading

* https://github.com/cloudfoundry/java-buildpack-security-provider/tree/master/src/main/java/org/cloudfoundry/security
* https://github.com/scholzj/AliasKeyManager
* https://tersesystems.com/blog/2014/07/07/play-tls-example-with-client-authentication/
