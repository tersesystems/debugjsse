package com.tersesystems.debugjsse;


import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;

public interface Debug {
    void enter(X509ExtendedTrustManager delegate, String method, Object[] args);
    void enter(X509ExtendedKeyManager delegate, String method, Object[] args);

    <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args);
    <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args);

    void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args);
    void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args);
}
