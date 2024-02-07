package FileServerGateway;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static FileServerGateway.HTTPExchanger.notSupported;

public class FSGateway implements Runnable {

    private final DatagramSocket udp;
    private final ServerManager manager;
    private HttpServer server;
    private Executor executor;



    public FSGateway(int UDPPort, InetAddress UDPAddress, boolean secure) throws IOException {
        udp = new DatagramSocket(UDPPort, UDPAddress);
        if (secure)
            this.initHttpsServer( new InetSocketAddress(443), FSGProperties.Backlog);
        else
            this.initHTTPServer( new InetSocketAddress(80), FSGProperties.Backlog);
        manager = new ServerManager(udp);
    }

    public FSGateway(int UDPPort, InetAddress UDPAddress, int TCPPort, boolean secure) throws IOException {
        udp = new DatagramSocket(UDPPort, UDPAddress);
        InetSocketAddress address = new InetSocketAddress(TCPPort);
        if (secure)
            this.initHttpsServer(address, FSGProperties.Backlog);
        else
            this.initHTTPServer(address, FSGProperties.Backlog);

        manager = new ServerManager(udp);
    }

    public FSGateway(int UDPPort, InetAddress UDPAddress, InetAddress TCPAddress, int TCPPort, boolean secure) throws IOException {
        udp = new DatagramSocket(UDPPort, UDPAddress);
        InetSocketAddress address = new InetSocketAddress(TCPAddress, TCPPort);
        if (secure)
            this.initHttpsServer(address, FSGProperties.Backlog);
        else
            this.initHTTPServer(address, FSGProperties.Backlog);
        manager = new ServerManager(udp);
    }


    private void handleRequest(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> manager.GET(exchange);
            case "HEAD" -> manager.HEAD(exchange);
            case "CONNECT" -> exchange.sendResponseHeaders(200, -1);
            case "POST", "OPTIONS", "TRACE", "PUT", "DELETE", "PATCH" -> notSupported(exchange);
            default -> HTTPExchanger.internalError(exchange);
        }
    }


    @Override
    public void run() {
        server.start();
        System.out.println("Server Started");
        manager.run();
    }

    private void initExecutor() {
        if(FSGProperties.MaxRequests>1) {
            executor= Executors.newFixedThreadPool(FSGProperties.MaxRequests);
        }
        else if (FSGProperties.MaxRequests==1){
            executor= Executors.newSingleThreadExecutor();
        }
        else {
            executor = Executors.newCachedThreadPool();
        }
    }

    private void initHTTPServer(InetSocketAddress inetSocketAddress, int backlog) throws IOException {
        initExecutor();
        server = HttpServer.create(inetSocketAddress, backlog);
        server.setExecutor(executor);

        HttpContext context = server.createContext("/");
        context.setHandler(this::handleRequest);
        HTTPExchanger.setServerName(server.getAddress().getHostName());
    }

    public void initHttpsServer(InetSocketAddress address, int Backlog) throws IOException {

        try {
            initExecutor();
            // Set up the socket address
            // Initialise the HTTPS server
            HttpsServer httpsServer = HttpsServer.create(address, Backlog);
            SSLContext sslContext = SSLContext.getInstance(FSGProperties.SSLProtocol);

            // Initialise the keystore
            KeyStore ks = KeyStore.getInstance(FSGProperties.KeyStoreAlgorithm);
            FileInputStream fis = new FileInputStream(FSGProperties.KeystoreFile);
            ks.load(fis, FSGProperties.Password.toCharArray());

            // Set up the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(FSGProperties.KeyManagerAlgorithm);
            kmf.init(ks, FSGProperties.Password.toCharArray());

            // Set up the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(FSGProperties.TrustManagerAlgorithm);
            tmf.init(ks);

            // Set up the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        // Initialise the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // Get the default parameters
                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(defaultSSLParameters);
                    } catch (Exception e) {
                        System.out.println("Failed to create HTTPS port");
                    }
                }
            });

            httpsServer.createContext("/", this::handleRequest);
            httpsServer.setExecutor(executor); // creates a default executor
            server = httpsServer;
            HTTPExchanger.setServerName(server.getAddress().getHostName());

        } catch (Exception e) {
            throw new IOException("Failed to create HTTPS server on address " + address + "\n" + e.getMessage(), e);
        }
    }
}
