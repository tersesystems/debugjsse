package com.tersesystems.debugjsse;

import javax.net.ssl.*;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DebugKeyManagerFactorySpi extends KeyManagerFactorySpi {
    protected KeyManagerFactory factory;

    protected Debug debug = DebugJSSEProvider.debug;

    /**
     * @return The key manager algorithm that this factory SPI will wrap.
     */
    public abstract String getAlgorithm();

    protected void engineInit(KeyStore keyStore, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Object[] args = { keyStore, chars };
        debug.enter(factory, args);
        try {
            factory = KeyManagerFactory.getInstance(getAlgorithm(), "SunJSSE");
            factory.init(keyStore, chars);
            debug.exit(factory, null, args);
        } catch (RuntimeException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (KeyStoreException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (NoSuchAlgorithmException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (UnrecoverableKeyException e) {
            debug.exception(factory, e, args);
            throw e;
        } catch (NoSuchProviderException e) {
            debug.exception(factory, e, args);
            throw new RuntimeException(e);
        }
    }

    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        Object[] args = { managerFactoryParameters };
        debug.enter(factory, args);
        try {
            factory = KeyManagerFactory.getInstance(getAlgorithm());
            factory.init(managerFactoryParameters);
            debug.exit(factory, null, args);
        }  catch (RuntimeException e) {
            debug.exception(factory, e, args);
            throw e;
        }catch (NoSuchAlgorithmException e) {
            debug.exception(factory, e, args);
            throw new InvalidAlgorithmParameterException(e);
        } catch (InvalidAlgorithmParameterException e) {
            debug.exception(factory, e, args);
            throw e;
        }
    }

    protected KeyManager[] engineGetKeyManagers() {
        debug.enter(factory, null);
        KeyManager[] result = wrapKeyManagers(factory.getKeyManagers());
        return debug.exit(factory, result, null);
    }

    protected KeyManager[] wrapKeyManagers(KeyManager[] originalKeyManagers)
    {
        List<KeyManager> wrapped = new ArrayList<KeyManager>();

        for (int i = 0; i < originalKeyManagers.length; i++) {
            KeyManager originalKeyManager = originalKeyManagers[i];
            if (originalKeyManager instanceof X509ExtendedKeyManager) {
                KeyManager wrap = new DebugX509ExtendedKeyManager((X509ExtendedKeyManager) originalKeyManager, debug);
                wrapped.add(wrap);
            } else {
                throw new IllegalStateException("original key manager is not an instance of X509ExtendedKeyManager: " + originalKeyManager);
            }
        }

        return wrapped.toArray(new KeyManager[0]);
    }
}
