import com.tersesystems.debugjsse.AbstractDebug;
import com.tersesystems.debugjsse.Debug;
import com.tersesystems.debugjsse.DebugJSSEProvider;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;

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

    //
    //    private static KeyStore emptyStore() throws Exception {
    //        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    //        ks.load(null, "changeit".toCharArray());
    //
    //        long validSecs = (long) 365 * 24 * 60 * 60;
    //        CertAndKeyGen certGen = generateCertificate();
    //        X509Certificate x509Certificate =
    //        certGen.getSelfCertificate(
    //                new X500Name("CN=My Application,O=My Organisation,L=My City,C=DE"), validSecs);
    //        ks.setKeyEntry("mycert", certGen.getPrivateKey(), "changeit".toCharArray(),
    //                new X509Certificate[] { x509Certificate });
    //
    //        return ks;
    //    }
    //
    //    static CertAndKeyGen generateCertificate() throws Exception {
    //        CertAndKeyGen certGen = new CertAndKeyGen("RSA", "SHA256WithRSA", null);
    //        certGen.generate(2048);
    //        return certGen;
    //    }
}
