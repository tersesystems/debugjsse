package com.tersesystems.debugjsse;


import javax.net.ssl.*;
import java.security.KeyStore;

public interface Debug {
    void enter(X509ExtendedTrustManager delegate, String method, Object[] args);
    void enter(X509ExtendedKeyManager delegate, String method, Object[] args);

    <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args);
    <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args);

    void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args);
    void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args);

    void enter(KeyManagerFactory factory, Object[] args);
    void enter(TrustManagerFactory factory, Object[] args);

    <T> T exit(TrustManagerFactory factory, T result, Object[] args);
    <T> T exit(KeyManagerFactory factory, T result, Object[] args);

    void exception(TrustManagerFactory factory, Exception e, Object[] args);
    void exception(KeyManagerFactory factory, Exception e, Object[] args);
}
