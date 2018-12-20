package com.ss.android.gamecommon.thread;

import android.annotation.SuppressLint;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSSSLSocketFactory extends SSLSocketFactory {
    private SSLContext sslContext = SSLContext.getInstance("TLS");

    @SuppressLint("TrulyRandom")
    public SSSSLSocketFactory(KeyStore keyStore) throws KeyManagementException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException {
        super(keyStore);
        SSTrustManager trustManager = new SSTrustManager(this);
        sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
    }

    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
            throws IOException {
        return sslContext.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
    }

    private static class SSTrustManager implements X509TrustManager {
        SSTrustManager(SSLSocketFactory socketFactory) {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }
}
