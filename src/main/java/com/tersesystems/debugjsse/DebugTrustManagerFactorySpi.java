package com.tersesystems.debugjsse;

import javax.net.ssl.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DebugTrustManagerFactorySpi extends TrustManagerFactorySpi {

    protected Debug debug = DebugJSSEProvider.debug;

    protected TrustManagerFactory factory;

    public DebugTrustManagerFactorySpi() throws NoSuchAlgorithmException {
        factory = TrustManagerFactory.getInstance(DebugJSSEProvider.DEFAULT_TRUSTMANAGER_ALGORITHM);
    }

    @Override
    protected void engineInit(KeyStore ks) throws KeyStoreException {
        Object[] args = {ks};
        debug.enter(factory, args);
        try {
            factory.init(ks);
            debug.exit(factory, null, args);
        } catch (RuntimeException re) {
            debug.exception(factory, re, args);
            throw re;
        } catch (KeyStoreException e) {
            debug.exception(factory, e, args);
            throw e;
        }
    }

    @Override
    protected void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
        Object[] args = { spec };
        debug.enter(factory, args);
        try {
            factory.init(spec);
            debug.exit(factory, null, args);
        } catch (RuntimeException re) {
            debug.exception(factory, re, args);
            throw re;
        } catch (InvalidAlgorithmParameterException e) {
            debug.exception(factory, e, args);
            throw e;
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
            if (originalTrustManagers[i] instanceof X509ExtendedTrustManager) {
                TrustManager wrap = new DebugX509ExtendedTrustManager((X509ExtendedTrustManager) originalTrustManagers[i], debug);
                wrapped.add(wrap);
            }
        }

        return wrapped.toArray(new TrustManager[0]);
    }
}

