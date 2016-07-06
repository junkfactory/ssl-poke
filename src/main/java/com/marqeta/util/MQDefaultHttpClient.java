package com.marqeta.util;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by amontecillo on 7/6/16.
 */
public class MQDefaultHttpClient extends DefaultHttpClient {

    public MQDefaultHttpClient() {

        try {

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            //initialize ssl context with default keymanagers and trustmanagers
            sslContext.init(null, null, new SecureRandom());
            //custom socket factory accepting all hosts and tlsv1.2
            //supports wild cards in hostname verifier
            MQSSLSocketFactory socketFactory = new MQSSLSocketFactory(sslContext, SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
