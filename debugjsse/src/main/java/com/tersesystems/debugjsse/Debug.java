package com.tersesystems.debugjsse;

import javax.net.ssl.*;

public interface Debug {

    // Java type system not up to extending multiple generic types due to type erasure
    // https://softwareengineering.stackexchange.com/questions/219427/implementing-multiple-generic-interfaces-in-java
    //    public interface GenericDebug<T> {
    //        void enter(T delegate, String method, Object[] args);
    //        <R> R exit(T delegate, String method, R result, Object[] args);
    //        void exception(T delegate, String method, Exception e, Object[] args);
    //    }

    void enter(X509ExtendedTrustManager delegate, String method, Object[] args);
    <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args);
    void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args);

    void enter(X509ExtendedKeyManager delegate, String method, Object[] args);
    <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args);
    void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args);

    void enter(KeyManagerFactory factory, Object[] args);
    <T> T exit(KeyManagerFactory factory, T result, Object[] args);
    void exception(KeyManagerFactory factory, Exception e, Object[] args);

    void enter(TrustManagerFactory factory, Object[] args);
    <T> T exit(TrustManagerFactory factory, T result, Object[] args);
    void exception(TrustManagerFactory factory, Exception e, Object[] args);

    void enter(SSLContext context, String method, Object[] args);
    <T> T exit(SSLContext context, String method,  T result, Object[] args);
    void exception(SSLContext context, String method, Exception e, Object[] args);

}
