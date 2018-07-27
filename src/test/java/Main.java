import com.tersesystems.debugjsse.Debug;
import com.tersesystems.debugjsse.DebugJSSEProvider;
import org.slf4j.ext.XLoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;

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
