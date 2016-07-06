package com.marqeta.util;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpParams;
import sun.security.ssl.SSLSocketImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by amontecillo on 7/6/16.
 */
public class MQSSLSocketFactory extends SSLSocketFactory {

    public MQSSLSocketFactory(SSLContext sslContext) {
        super(sslContext);
    }

    public MQSSLSocketFactory(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
        super(sslContext, hostnameVerifier);
    }

    @Override
    protected void prepareSocket(SSLSocket socket) throws IOException {
        socket.setEnabledProtocols(new String[] {"TLSv1", "TLSv1.1", "TLSv1.2"});
        super.prepareSocket(socket);
    }

    @Override
    public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException {
        setHost(socket, remoteAddress.getHostName());
        return super.connectSocket(socket, remoteAddress, localAddress, params);
    }

    @Override
    public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
        setHost(socket, host);
        return super.connectSocket(socket, host, port, localAddress, localPort, params);
    }

    /**
     * Set the host if the socket is an instance of SSLSocketImpl for SNI support
     * @param socket
     * @param host
     */
    private void setHost(Socket socket, String host) {
        if (socket instanceof SSLSocket) {
            if (socket instanceof SSLSocketImpl) {
                ((SSLSocketImpl) socket).setHost(host);
            } else {
                throw new IllegalArgumentException("Socket is not an instanceof SSLSocketImpl: " + socket.getClass().getName());
            }
        }
    }
}
