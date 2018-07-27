package com.tersesystems.debugjsse;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class DebugX509ExtendedKeyManager extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager delegate;
    private final Debug debug;

    public DebugX509ExtendedKeyManager(X509ExtendedKeyManager delegate, Debug debug) {
        this.delegate = delegate;
        this.debug = debug;
    }

    @Override
    public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine engine) {
        final String msg = String.format("chooseEngineClientAlias: keyTypes = %s, issuers = %s, engine = %s",
                Arrays.toString(keyTypes), Arrays.toString(issuers), engine);
        debug.enter(msg);
        String result = delegate.chooseEngineClientAlias(keyTypes, issuers, engine);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        final String msg = String.format("chooseEngineServerAlias: keyType = %s, issuers = %s, engine = %s",
                keyType, Arrays.toString(issuers), engine);
        debug.enter(msg);
        String result = delegate.chooseEngineServerAlias(keyType, issuers, engine);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        final String msg = String.format("getClientAliases: keyType = %s, issuers = %s",
                keyType, Arrays.toString(issuers));
        debug.enter(msg);
        String[] result = delegate.getClientAliases(keyType, issuers);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        final String msg = String.format("chooseClientAlias: keyTypes = %s, issuers = %s, socket = %s",
                Arrays.toString(keyTypes), Arrays.toString(issuers), socket);
        debug.enter(msg);
        String result = delegate.chooseClientAlias(keyTypes, issuers, socket);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        final String msg = String.format("chooseClientAlias: keyType = %s, issuers = %s",
                keyType, Arrays.toString(issuers));
        debug.enter(msg);
        String[] result = delegate.getServerAliases(keyType, issuers);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public String chooseServerAlias(String alias, Principal[] issuers, Socket socket) {
        final String msg = String.format("chooseServerAlias: alias = %s, issuers = %s, socket = %s",
                alias, Arrays.toString(issuers), socket);
        debug.enter(msg);
        String result = delegate.chooseServerAlias(alias, issuers, socket);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        final String msg = String.format("getCertificateChain: alias = %s", alias);
        debug.enter(msg);
        X509Certificate[] result = delegate.getCertificateChain(alias);
        debug.exit(msg, result);
        return result;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        final String msg = String.format("getPrivateKey: alias = %s", alias);
        debug.enter(msg);
        PrivateKey result = delegate.getPrivateKey(alias);
        debug.exit(msg, result);
        return result;
    }
}
