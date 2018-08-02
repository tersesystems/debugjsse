package com.tersesystems.debugjsse;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class DebugX509ExtendedKeyManager extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager delegate;
    private final Debug debug;

    public DebugX509ExtendedKeyManager(X509ExtendedKeyManager delegate, Debug debug) {
        this.delegate = delegate;
        this.debug = debug;
    }

    public X509ExtendedKeyManager getDelegate() {
        return delegate;
    }

    @Override
    public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine engine) {
        final String method = "chooseEngineClientAlias";
        final Object[] args = new Object[] { keyTypes, issuers, engine };

        debug.enter(delegate, method, args);
        try {
            String result = delegate.chooseEngineClientAlias(keyTypes, issuers, engine);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        final String method = "chooseEngineServerAlias";
        final Object[] args = new Object[] { keyType, issuers, engine };

        debug.enter(delegate, method, args);
        try {
            String result = delegate.chooseEngineServerAlias(keyType, issuers, engine);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        final String method = "getClientAliases";
        final Object[] args = new Object[] {  keyType, issuers };

        debug.enter(delegate, method, args);
        try {
            String[] result = delegate.getClientAliases(keyType, issuers);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        final String method = "chooseClientAlias";
        final Object[] args = new Object[] { keyTypes, issuers };

        debug.enter(delegate, method, args);
        try {
            String result = delegate.chooseClientAlias(keyTypes, issuers, socket);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        final String method = "getServerAliases";
        final Object[] args = new Object[] { keyType, issuers };

        debug.enter(delegate, method, args);
        try {
            String[] result = delegate.getServerAliases(keyType, issuers);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String chooseServerAlias(String alias, Principal[] issuers, Socket socket) {
        final String method = "chooseServerAlias";
        final Object[] args = new Object[] { alias, issuers, socket };

        debug.enter(delegate, method, args);
        try {
            String result = delegate.chooseServerAlias(alias, issuers, socket);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        final String method = "getCertificateChain";
        final Object[] args = new Object[] { alias };

        debug.enter(delegate, method, args);
        try {
            X509Certificate[] result = delegate.getCertificateChain(alias);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        final String method = "getPrivateKey";
        final Object[] args = new Object[] { alias };

        debug.enter(delegate, method, args);
        try {
            PrivateKey result = delegate.getPrivateKey(alias);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "DebugX509ExtendedKeyManager@" + this.hashCode() + "(" + delegate.toString() + ")";
    }
}
