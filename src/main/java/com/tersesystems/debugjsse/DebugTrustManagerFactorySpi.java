package com.tersesystems.debugjsse;

import javax.net.ssl.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DebugTrustManagerFactorySpi extends TrustManagerFactorySpi {

    protected Debug debug = DebugJSSEProvider.debug;

    protected TrustManagerFactory factory;

    /**
     * @return The key manager algorithm that this factory SPI will wrap.
     */
    public abstract String getAlgorithm();

    public DebugTrustManagerFactorySpi() throws NoSuchAlgorithmException, NoSuchProviderException {
        factory = TrustManagerFactory.getInstance(getAlgorithm(), "SunJSSE");
    }

    @Override
    protected void engineInit(KeyStore ks) throws KeyStoreException {
        Object[] args = {ks};
        debug.enter(factory, args);
        try {
            factory.init(ks);
            debug.exit(factory, null, args);
        } catch (KeyStoreException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (RuntimeException re) {
            debug.exception(factory, re, args);
            throw re;
        }
    }

    @Override
    protected void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
        Object[] args = { spec };
        debug.enter(factory, args);
        try {
            factory.init(spec);
            debug.exit(factory, null, args);
        } catch (InvalidAlgorithmParameterException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (RuntimeException re) {
            debug.exception(factory, re, args);
            throw re;
        }
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        debug.enter(factory, null);
        TrustManager[] result = wrapTrustManagers(factory.getTrustManagers());
        debug.exit(factory, result, null);
        return result;
    }

    protected TrustManager[] wrapTrustManagers(TrustManager[] originalTrustManagers)
    {
        List<TrustManager> wrapped = new ArrayList<TrustManager>();

        for (int i = 0; i < originalTrustManagers.length; i++) {
            TrustManager originalTrustManager = originalTrustManagers[i];
            if (originalTrustManager instanceof X509ExtendedTrustManager) {
                TrustManager wrap = new DebugX509ExtendedTrustManager((X509ExtendedTrustManager) originalTrustManager, debug);
                wrapped.add(wrap);
            } else {
                throw new IllegalStateException("original trust manager is not an instance of X509ExtendedTrustManager: " + originalTrustManager);
            }
        }

        return wrapped.toArray(new TrustManager[0]);
    }
}

