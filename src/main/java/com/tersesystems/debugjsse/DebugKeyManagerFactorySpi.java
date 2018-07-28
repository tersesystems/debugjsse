package com.tersesystems.debugjsse;

import javax.net.ssl.*;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class DebugKeyManagerFactorySpi extends KeyManagerFactorySpi {
    protected KeyManagerFactory factory;

    protected Debug debug = DebugJSSEProvider.debug;

    protected void engineInit(KeyStore keyStore, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Object[] args = { keyStore, chars };
        debug.enter(factory, args);
        try {
            factory = KeyManagerFactory.getInstance(DebugJSSEProvider.DEFAULT_KEYMANAGER_ALGORITHM);
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
        }
    }

    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        Object[] args = { managerFactoryParameters };
        debug.enter(factory, args);
        try {
            factory = KeyManagerFactory.getInstance(DebugJSSEProvider.DEFAULT_KEYMANAGER_ALGORITHM);
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
            if (originalKeyManagers[i] instanceof X509ExtendedKeyManager) {
                KeyManager wrap = new DebugX509ExtendedKeyManager((X509ExtendedKeyManager) originalKeyManagers[i], debug);
                wrapped.add(wrap);
            }
        }

        return wrapped.toArray(new KeyManager[0]);
    }
}
