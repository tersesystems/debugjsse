package com.tersesystems.debugjsse;

import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.util.Arrays;

public class SystemOutDebug implements Debug {

    @Override
    public void enter(X509ExtendedTrustManager delegate, String method, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        enter(msg);
    }

    @Override
    public void enter(X509ExtendedKeyManager delegate, String method, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        enter(msg);
    }

    @Override
    public <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        exit(msg, result);
        return result;
    }

    @Override
    public <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        exit(msg, result);
        return result;
    }

    @Override
    public void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        enter(msg);
    }

    @Override
    public void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args) {
        String msg = String.format("%s: args = %s with delegate = %s", method, Arrays.toString(args), delegate);
        exception(msg, e);
    }

    protected void enter(String message) {
        System.out.println("enter: message = [" + message + "]");
    }

    protected void exit(String message, Object result) {
        System.out.println("exit: message = " + message + ", result = " + result);
    }

    protected void exception(String message, Exception e) {
        System.out.println("exception: message = [" + message + "], e = [" + e + "]");
    }

}
