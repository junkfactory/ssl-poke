package com.marqeta.util;

import org.apache.commons.lang3.ArrayUtils;

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
        if (args.length != 2) {
            System.out.println((new StringBuilder()).append("Usage: ").append(App.class.getName()).append(" <host> <port>").toString());
            System.exit(1);
        }
        try {
            if (new App().poke(args[0], Integer.parseInt(args[1]))) {
                System.out.println("Successfully connected");
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean poke(String host, int port) throws IOException {

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket)sslsocketfactory.createSocket(host, port);
        sslsocket.setEnabledProtocols(new String[] {
                "SSLv3", "TLSv1", "TLSv1.2"
        });

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
}
