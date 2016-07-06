package com.marqeta.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import sun.security.ssl.SSLSocketImpl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if (args.length != 3) {
            usage();
        }

        App app = new App();

        try {

            switch (args[0]) {
                case "-p": {
                    if (app.poke(args[1], Integer.parseInt(args[2]))) {
                        System.out.println("Successfully connected");
                    }
                    break;
                }
                case "-t": {
                    app.testPostWithHttpClient(args[1], args[2]);
                    break;
                }
                default:
                    usage();
            }

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println((new StringBuilder()).append("Usage: ").append(App.class.getName()).append(" -p <host> <port> or -t <url> <payload>").toString());
        System.exit(1);
    }

    public boolean poke(String host, int port) throws IOException {

        CryptoProviders cryptoProviders = new CryptoProviders();
        System.out.println(cryptoProviders.getProviders());

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket)sslsocketfactory.createSocket(host, port);
        sslsocket.setEnabledProtocols(new String[] {
                "SSLv3", "TLSv1", "TLSv1.2"
        });

        if (sslsocket instanceof SSLSocketImpl) {
            System.out.println("Setting host to: " + host);
            ((SSLSocketImpl)sslsocket).setHost(host);
        }

        System.out.println("ENABLED CIPHER SUITES");
        System.out.println(ArrayUtils.toString(sslsocket.getEnabledCipherSuites()));
        System.out.println("ENABLED PROTOCOLS");
        System.out.println(ArrayUtils.toString(sslsocket.getEnabledProtocols()));
        InputStream in = sslsocket.getInputStream();
        OutputStream out = sslsocket.getOutputStream();
        out.write(1);
        for(; in.available() > 0; System.out.print(in.read()));
        return true;

    }

    public void testPostWithHttpClient(String url, String payload) throws IOException {

        MQDefaultHttpClient client = new MQDefaultHttpClient();

        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setEntity(new StringEntity(payload));

        HttpResponse response = client.execute(request);
        System.out.println(response.toString());
    }
}
