package com.example.testingcertificatepinning;

import android.net.http.X509TrustManagerExtensions;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpUrlConnectPinner {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void validatePinning(
            X509TrustManagerExtensions trustManagerExt,
            HttpsURLConnection conn, Set<String> validPins)
            throws SSLException {
        String certChainMsg = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            List<X509Certificate> trustedChain =
                    trustedChain(trustManagerExt, conn);
            for (X509Certificate cert : trustedChain) {
                byte[] publicKey = cert.getPublicKey().getEncoded();
                md.update(publicKey, 0, publicKey.length);
                String pin = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);
                certChainMsg += "    sha256/" + pin + " : " +
                        cert.getSubjectDN().toString() + "\n";
                if (validPins.contains(pin)) {
                    return;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException(e);
        }
        throw new SSLPeerUnverifiedException("Certificate pinning " +
                "failure\n  Peer certificate chain:\n" + certChainMsg);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private List<X509Certificate> trustedChain(
            X509TrustManagerExtensions trustManagerExt,
            HttpsURLConnection conn) throws SSLException {
        Certificate[] serverCerts = conn.getServerCertificates();
        X509Certificate[] untrustedCerts = Arrays.copyOf(serverCerts,
                serverCerts.length, X509Certificate[].class);
        String host = conn.getURL().getHost();
        try {
            return trustManagerExt.checkServerTrusted(untrustedCerts,
                    "RSA", host);
        } catch (CertificateException e) {
            throw new SSLException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean testPinning() {
        TrustManagerFactory trustManagerFactory =
                null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init((KeyStore) null);
// Find first X509TrustManager in the TrustManagerFactory
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }
            X509TrustManagerExtensions trustManagerExt =
                    new X509TrustManagerExtensions(x509TrustManager);

            URL url = new URL("https://www.appmattus.com/");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.connect();
            Set<String> validPins = Collections.singleton
                    ("HHsQHTnVqDQ6b+oXEu1HDYHlnH7iLMqKvAi4MUDW9hc=");
            validPins.add("YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=");
            validatePinning(trustManagerExt, urlConnection, validPins);
            return true;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        return false;
    }
}
