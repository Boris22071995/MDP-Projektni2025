package net.etfbl.mdp.server;

import java.io.*;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class SupplierServer implements Runnable {
	
    private String port;
    private String supplierName;
    private ExecutorService pool;
    private int PORT = ConfigurationLoader.getInt("supplier.port");
    private static String KEY_STORE_PATH = ConfigurationLoader.getString("keystore.path");
    private static final String KEY_STORE_PASSWORD = ConfigurationLoader.getString("keystore.pass");

    private static final Logger log = AppLogger.getLogger();
    public SupplierServer(String supplierName, String port) {
        this.supplierName = supplierName;
        this.port = port;
        this.PORT = Integer.parseInt(port);
        this.pool = Executors.newFixedThreadPool(5);
    }

    @Override
    public void run() {
    	try {
            File f = new File(KEY_STORE_PATH);
            KEY_STORE_PATH = f.getAbsolutePath();

            System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
            System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);

            char[] passphrase = KEY_STORE_PASSWORD.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(KEY_STORE_PATH)) {
                ks.load(fis, passphrase);
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);

            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);

            while (true) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                SupplierHandler handler = new SupplierHandler(socket, supplierName);
                new Thread(handler).start();
            }

        } catch (Exception e) {
        	log.severe("Error while starting server");
            e.printStackTrace();
        }
    }

}
