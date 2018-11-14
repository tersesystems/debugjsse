package com.tersesystems.debugjsse;

import org.junit.Test;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class DebugTest {

    @Test
    public void testChooseClientAlias() {
        X509ExtendedKeyManager delegate = new X509ExtendedKeyManager() {
            @Override
            public String[] getClientAliases(String s, Principal[] principals) {
                return new String[0];
            }

            @Override
            public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
                return null;
            }

            @Override
            public String[] getServerAliases(String s, Principal[] principals) {
                return new String[0];
            }

            @Override
            public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
                return null;
            }

            @Override
            public X509Certificate[] getCertificateChain(String s) {
                return new X509Certificate[0];
            }

            @Override
            public PrivateKey getPrivateKey(String s) {
                return null;
            }
        };

        final Debug debug = new AbstractDebug() {
            @Override
            public void enter(String message) {
                System.out.println(message);
            }

            @Override
            public void exit(String message) {
                System.out.println(message);
            }

            @Override
            public void exception(String message, Exception e) {
                System.out.println(message);
            }
        };

        DebugX509ExtendedKeyManager keyManager = new DebugX509ExtendedKeyManager(delegate, debug);
        String[] keyTypes = { "DSA", "RSA" };
        Principal[] issuers = {};
        keyManager.chooseClientAlias(keyTypes, issuers, null);
    }
}
