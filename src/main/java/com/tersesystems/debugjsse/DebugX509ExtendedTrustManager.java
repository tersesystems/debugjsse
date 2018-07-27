package com.tersesystems.debugjsse;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class DebugX509ExtendedTrustManager extends X509ExtendedTrustManager {

    private final X509ExtendedTrustManager delegate;
    private final Debug debug;

    public DebugX509ExtendedTrustManager(X509ExtendedTrustManager delegate, Debug debug) {
        this.delegate = delegate;
        this.debug = debug;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        final String msg = String.format("checkClientTrusted: chain = %s, authType = %s",
                Arrays.toString(chain), authType);
        debug.enter(msg);
        try {
            delegate.checkClientTrusted(chain, authType);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        final String msg = String.format("checkClientTrusted: chain = %s, authType = %s, socket = %s",
                Arrays.toString(chain), authType, socket);
        debug.enter(msg);
        try {
            delegate.checkClientTrusted(chain, authType, socket);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine sslEngine) throws CertificateException {
        final String msg = String.format("checkClientTrusted: chain = %s, authType = %s, sslEngine = %s",
                Arrays.toString(chain), authType, sslEngine);
        debug.enter(msg);
        try {
            delegate.checkClientTrusted(chain, authType, sslEngine);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        final String msg = String.format("checkServerTrusted: chain = %s, authType = %s",
                Arrays.toString(chain), authType);
        debug.enter(msg);
        try {
            delegate.checkServerTrusted(chain, authType);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        final String msg = String.format("checkServerTrusted: chain = %s, authType = %s, socket = %s",
                Arrays.toString(chain), authType, socket);
        debug.enter(msg);
        try {
            delegate.checkServerTrusted(chain, authType, socket);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine sslEngine) throws CertificateException {
        final String msg = String.format("checkServerTrusted: chain = %s, authType = %s, sslEngine = %s",
                Arrays.toString(chain), authType, sslEngine);
        debug.enter(msg);
        try {
            delegate.checkServerTrusted(chain, authType, sslEngine);
        } catch (CertificateException e) {
            debug.exception(msg, e);
            throw e;
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        final String msg = "getAcceptedIssuers";
        debug.enter(msg);
        X509Certificate[] result = delegate.getAcceptedIssuers();
        debug.exit(msg, result);
        return result;
    }
}