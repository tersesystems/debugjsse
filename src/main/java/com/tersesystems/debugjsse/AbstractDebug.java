package com.tersesystems.debugjsse;

import javax.net.ssl.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public abstract class AbstractDebug implements Debug {

    public abstract void enter(String message);

    public abstract void exit(String message);

    public abstract void exception(String message, Exception e);

    @Override
    public void enter(X509ExtendedTrustManager delegate, String method, Object[] args) {
        enter("enter: " + trustManagerMethod(delegate, method, args));
    }

    @Override
    public void enter(X509ExtendedKeyManager delegate, String method, Object[] args) {
        enter("enter: " + keyManagerMethod(delegate, method, args));
    }

    @Override
    public void enter(KeyManagerFactory factory, Object[] args) {
        exit("enter: " + keyManagerFactoryMethod(factory, args));
    }

    @Override
    public void enter(TrustManagerFactory factory, Object[] args) {
        exit("enter: " + trustManagerFactoryMethod(factory, args));
    }

    @Override
    public <T> T exit(X509ExtendedTrustManager delegate, String method, T result, Object[] args) {
        exit("exit:  " + trustManagerMethod(delegate, method, args) + " => " + resultString(result));
        return result;
    }

    @Override
    public <T> T exit(X509ExtendedKeyManager delegate, String method, T result, Object[] args) {
        exit("exit:  " + keyManagerMethod(delegate, method, args) + " => " + resultString(result));
        return result;
    }

    @Override
    public  <T> T  exit(TrustManagerFactory factory, T result, Object[] args) {
        exit("exit:  " + trustManagerFactoryMethod(factory, args) + " => " + resultString(result));
        return result;
    }

    @Override
    public  <T> T exit(KeyManagerFactory factory, T result, Object[] args) {
        exit("exit:  " + keyManagerFactoryMethod(factory, args) + " => " + resultString(result));
        return result;
    }

    @Override
    public void exception(X509ExtendedTrustManager delegate, String method, Exception e, Object[] args) {
        exception("exception: " + trustManagerMethod(delegate, method, args) + " ! " + e.toString(), e);
    }

    @Override
    public void exception(X509ExtendedKeyManager delegate, String method, Exception e, Object[] args) {
        exception("exception: " + keyManagerMethod(delegate, method, args) + " ! " + e.toString(), e);
    }

    @Override
    public void exception(TrustManagerFactory factory, Exception e, Object[] args) {
        exception("exception: " + trustManagerFactoryMethod(factory, args) + " ! " + e.toString(), e);
    }

    @Override
    public void exception(KeyManagerFactory factory, Exception e, Object[] args) {
        exception("exception: " + keyManagerFactoryMethod(factory, args) + " ! " + e.toString(), e);
    }

    protected String trustManagerMethod(X509ExtendedTrustManager delegate, String method, Object[] args) {
        if (method.equals("getAcceptedIssuers")) {
           return String.format("%s.%s()", delegate, method);
        } else {
            X509Certificate[] chain = (X509Certificate[]) args[0];
            String authType = (String) args[1];

            String msg = String.format("%s.%s(chain = %s, authType = %s", delegate, method, debugChain(chain), authType);
            if (args.length == 3) {
                Object arg = args[2];
                if (arg instanceof SSLEngine) {
                    SSLEngine sslEngine = (SSLEngine) arg;
                    msg += "sslEngine = " + sslEngine;
                } else if (arg instanceof SSLSocket) {
                    SSLSocket sslSocket = (SSLSocket) arg;
                    msg += "sslSocket = " + sslSocket;
                } else {
                    msg += "arg = " + arg;
                }
            }
            return msg + ")";
        }
    }

    protected String keyManagerMethod(X509ExtendedKeyManager delegate, String method, Object[] args) {
        StringBuilder sb = new StringBuilder();
        if (method.equals("chooseEngineClientAlias")) {
            //public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
            String[] keyTypes = (String[]) args[0];
            Principal[] issuers = (Principal[]) args[1];
            SSLEngine engine = (SSLEngine) args[2];

            return String.format("%s.%s(keyTypes = %s, issuers = %s, engine = %s)", delegate, method, Arrays.toString(keyTypes), Arrays.toString(issuers), engine);
        } else if (method.equals("chooseEngineServerAlias")) {
            //public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
            String keyType = (String) args[0];
            Principal[] issuers = (Principal[]) args[1];
            SSLEngine engine = (SSLEngine) args[2];

            return String.format("%s.%s(keyTypes = %s, issuers = %s, engine = %s)", delegate, method, keyType, Arrays.toString(issuers), engine);
        } else if (method.equals("getClientAliases")) {
            //public String[] getClientAliases(String keyType, Principal[] issuers)
            String keyType = (String) args[0];
            Principal[] issuers = (Principal[]) args[1];

            return String.format("%s.%s(keyTypes = %s, issuers = %s, engine = %s)", delegate, method, keyType, Arrays.toString(issuers));
        } else if (method.equals("getServerAliases")) {
            //public String[] getServerAliases(String keyType, Principal[] issuers)
            String keyType = (String) args[0];
            Principal[] issuers = (Principal[]) args[1];

            return String.format("%s.%s(keyTypes = %s, issuers = %s)", delegate, method, keyType, Arrays.toString(issuers));
        } else if (method.equals("chooseClientAlias")) {
            //public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
            String keyType = (String) args[0];
            Principal[] issuers = (Principal[]) args[1];
            Socket socket = (Socket) args[2];

            return String.format("%s.%s(keyTypes = %s, issuers = %s, socket = %s)", delegate, method, keyType, Arrays.toString(issuers), socket);
        } else if (method.equals("chooseServerAlias")) {
            //public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
            String keyType = (String) args[0];
            Principal[] issuers = (Principal[]) args[1];
            Socket socket = (Socket) args[2];

            return String.format("%s.%s(keyTypes = %s, issuers = %s, socket = %s)", delegate, method, keyType, Arrays.toString(issuers), socket);
        } else if (method.equals("getCertificateChain")) {
            //public X509Certificate[] getCertificateChain(String alias)
            String alias = (String) args[0];

            return String.format("%s.%s(alias = %s)", delegate, method, alias);
        } else if (method.equals("getPrivateKey")) {
            //public PrivateKey getPrivateKey(String alias)
            String alias = (String) args[0];

            return String.format("%s.%s(alias = %s)", delegate, method, alias);
        } else {
            return "Unknown method " + method;
        }
    }

    protected String keyManagerFactoryMethod(KeyManagerFactory factory, Object[] args) {
        if (args == null) {
            return String.format("%s.getKeyManagers()", factory);
        }
        Object arg = args[0];
        if (arg instanceof ManagerFactoryParameters) {
            ManagerFactoryParameters managerFactoryParameters = (ManagerFactoryParameters) arg;
            return String.format("%s.init: managerFactoryParameters = %s", factory, managerFactoryParameters);
        } else {
            KeyStore keyStore = (KeyStore) arg;
            char[] password = (char[]) args[1];
            return String.format("%s.init: keyStore = %s, password = %s", factory, keystoreString(keyStore), Arrays.toString(password));
        }
    }

    protected String trustManagerFactoryMethod(TrustManagerFactory factory, Object[] args) {
        if (args == null) {
            return String.format("%s.getTrustManagers()", factory);
        }
        Object arg = args[0];
        if (arg instanceof KeyStore) {
            KeyStore keyStore = (KeyStore) arg;
            return String.format("%s.init: args = %s", factory, keystoreString(keyStore));
        } else {
            ManagerFactoryParameters spec = (ManagerFactoryParameters) args[0];
            return String.format("%s.init: args = %s", factory, spec);
        }
    }

    protected String keystoreString(KeyStore ks) {
        if (ks == null) {
            return "null";
        }
        try {
            List<String> list = new ArrayList<String>();
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate[] chain = ks.getCertificateChain(alias);
                list.add(alias + "=" + debugChain(chain));
            }
            return "KeyStore(" + list.toString() + ")";
        } catch (KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    protected String resultString(Object result) {
        if (result == null){
            return "null";
        }
        final String rs;
        if (result instanceof X509Certificate[]) {
            rs = debugChain((X509Certificate[]) result);
        } else if (result instanceof Object[]) {
            rs = Arrays.toString((Object[]) result);
        } else {
            rs = result.toString();
        }
        return rs;
    }

    protected String debugCertificate(Certificate cert) {
        if (cert == null) {
            return "null";
        }
        
        if (cert instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate) cert;
            return x509Certificate.getSubjectDN().getName();
        }

        return cert.toString();
    }

    protected String debugChain(Certificate[] chain) {
        if (chain == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < chain.length; i++) {
            sb.append(debugCertificate(chain[i]));
            if (i < chain.length - 1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
