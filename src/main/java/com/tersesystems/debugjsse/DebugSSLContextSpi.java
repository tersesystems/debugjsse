package com.tersesystems.debugjsse;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class DebugSSLContextSpi extends SSLContextSpi {

    public static final String SUN_JSSE = "SunJSSE";

    protected Debug debug = DebugJSSEProvider.debug;

    protected SSLContext delegate;

    public static class TLS10Context extends DebugSSLContextSpi {
        public TLS10Context() throws NoSuchAlgorithmException, NoSuchProviderException {
            delegate = SSLContext.getInstance("TLSv1", SUN_JSSE);
        }
    }

    public static class TLS11Context extends DebugSSLContextSpi {
        public TLS11Context() throws NoSuchAlgorithmException, NoSuchProviderException {
            delegate = SSLContext.getInstance("TLSv1.1", SUN_JSSE);
        }
    }

    public static class TLS12Context extends DebugSSLContextSpi {
        public TLS12Context() throws NoSuchAlgorithmException, NoSuchProviderException {
            delegate = SSLContext.getInstance("TLSv1.2", SUN_JSSE);
        }
    }

    public static class TLSContext extends DebugSSLContextSpi {
        public TLSContext() throws NoSuchAlgorithmException, NoSuchProviderException {
            delegate = SSLContext.getInstance("TLS", SUN_JSSE);
        }
    }

    public static class DefaultSSLContext extends DebugSSLContextSpi {
        public DefaultSSLContext() throws NoSuchAlgorithmException, NoSuchProviderException {
            delegate = SSLContext.getInstance("Default", SUN_JSSE);
        }
    }

    @Override
    protected void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr) throws KeyManagementException {
        String method = "init";
        Object[] args = { km, tm, sr };
        debug.enter(delegate, method, args);
        try {
            delegate.init(km, tm, sr);
            debug.exit(delegate, method, null, args);
        } catch (KeyManagementException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    protected SSLSocketFactory engineGetSocketFactory() {
        String method = "getSocketFactory";
        debug.enter(delegate, method, null);
        try {
            SSLSocketFactory result = delegate.getSocketFactory();
            return debug.exit(delegate, method, result, null);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, null);
            throw e;
        }
    }

    @Override
    protected SSLServerSocketFactory engineGetServerSocketFactory() {
        String method = "getServerSocketFactory";
        debug.enter(delegate, method, null);
        try {
            SSLServerSocketFactory result = delegate.getServerSocketFactory();
            return debug.exit(delegate, method, result, null);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, null);
            throw e;
        }
    }

    @Override
    protected SSLEngine engineCreateSSLEngine() {
        String method = "createSSLEngine";
        debug.enter(delegate, method, null);
        try {
            SSLEngine result = delegate.createSSLEngine();
            return debug.exit(delegate, method, result, null);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, null);
            throw e;
        }
    }

    @Override
    protected SSLEngine engineCreateSSLEngine(String host, int port) {
        String method = "createSSLEngine";
        Object[] args = {host, port};
        debug.enter(delegate, method, args);
        try {
            SSLEngine result = delegate.createSSLEngine(host, port);
            return debug.exit(delegate, method, result, args);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, args);
            throw e;
        }
    }

    @Override
    protected SSLSessionContext engineGetServerSessionContext() {
        String method = "getServerSessionContext";
        debug.enter(delegate, method, null);
        try {
            SSLSessionContext result = delegate.getServerSessionContext();
            return debug.exit(delegate, method, result, null);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, null);
            throw e;
        }
    }

    @Override
    protected SSLSessionContext engineGetClientSessionContext() {
        String method = "getClientSessionContext";
        debug.enter(delegate, method, null);
        try {
            SSLSessionContext result = delegate.getClientSessionContext();
            return debug.exit(delegate, method, result, null);
        } catch (RuntimeException e) {
            debug.exception(delegate, method, e, null);
            throw e;
        }
    }

}
