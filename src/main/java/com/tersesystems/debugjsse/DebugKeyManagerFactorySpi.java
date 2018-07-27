package com.tersesystems.debugjsse;

import javax.net.ssl.*;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class DebugKeyManagerFactorySpi extends KeyManagerFactorySpi {
    protected KeyManagerFactory originalFactory;

    protected Debug debug = DebugJSSEProvider.debug;

    protected void engineInit(KeyStore keyStore, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        originalFactory = KeyManagerFactory.getInstance(DebugJSSEProvider.DEFAULT_KEYMANAGER_ALGORITHM);
        originalFactory.init(keyStore, chars);
    }

    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException(
                "SunX509KeyManager does not use ManagerFactoryParameters so we don't either");
    }

    protected KeyManager[] engineGetKeyManagers() {
        return wrapKeyManagers(originalFactory.getKeyManagers());
    }

    protected KeyManager[] wrapKeyManagers(KeyManager[] originalKeyManagers)
    {
        List<KeyManager> wrapped = new ArrayList<KeyManager>();

        for (int i = 0; i < originalKeyManagers.length; i++) {
            if (originalKeyManagers[i] instanceof X509ExtendedKeyManager) {
                KeyManager wrap = new DebugX509ExtendedKeyManager((X509ExtendedKeyManager) originalKeyManagers[i], debug);
                wrapped.add(wrap);
            }
        }

        return wrapped.toArray(new KeyManager[0]);
    }
}
