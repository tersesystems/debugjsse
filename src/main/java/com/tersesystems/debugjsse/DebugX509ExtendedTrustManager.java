package com.tersesystems.debugjsse;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DebugX509ExtendedTrustManager extends X509ExtendedTrustManager {

    private final X509ExtendedTrustManager delegate;
    private final Debug debug;

    public DebugX509ExtendedTrustManager(X509ExtendedTrustManager delegate, Debug debug) {
        this.delegate = delegate;
        this.debug = debug;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        String method = "checkClientTrusted";
        Object[] args = new Object[] { chain, authType };

        debug.enter(delegate, method, args);
        try {
            delegate.checkClientTrusted(chain, authType);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        String method = "checkClientTrusted";
        Object[] args = new Object[] { chain, authType, socket };

        debug.enter(delegate, method, args);
        try {
            delegate.checkClientTrusted(chain, authType, socket);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine sslEngine) throws CertificateException {
        String method = "checkClientTrusted";
        Object[] args = new Object[] { chain, authType, sslEngine };

        debug.enter(delegate, method, args);
        try {
            delegate.checkClientTrusted(chain, authType, sslEngine);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        String method = "checkServerTrusted";
        Object[] args = new Object[] { chain, authType };

        debug.enter(delegate, method, args);
        try {
            delegate.checkServerTrusted(chain, authType);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        String method = "checkServerTrusted";
        Object[] args = new Object[] { chain, authType, socket };

        debug.enter(delegate, method, args);
        try {
            delegate.checkServerTrusted(chain, authType, socket);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine sslEngine) throws CertificateException {
        String method = "checkServerTrusted";
        Object[] args = new Object[] { chain, authType, sslEngine };

        debug.enter(delegate, method, args);
        try {
            delegate.checkServerTrusted(chain, authType, sslEngine);
            debug.exit(delegate, method, null, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        final String method = "getAcceptedIssuers";
        Object[] args = new Object[] { };

        debug.enter(delegate, method, args);
        try {
            X509Certificate[] result = delegate.getAcceptedIssuers();
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }
}