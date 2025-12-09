package net.etfbl.mdp.securechat;


import javax.net.ssl.*;

import net.etfbl.mdp.util.AppLogger;

import java.io.*;
import java.security.KeyStore;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.File;

public class SSLChatClientTest {

	private static final Logger log = AppLogger.getLogger();
    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread receiverThread;
    
    private Consumer<String> onMessage; // GUI callback

    public SSLChatClientTest(Consumer<String> onMessage) {
        this.onMessage = onMessage;
    }
    
    public void connect(String username) throws Exception {
    	      String HOST = "localhost";
    	      int PORT = 9443;
    	      String TRUST_STORE_PATH = "./truststore.jks";
    	      String TRUST_STORE_PASSWORD = "servis123";
    	      File f = new File(TRUST_STORE_PATH);
              TRUST_STORE_PATH = f.getAbsolutePath();
              System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
              System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);
              char[] passphrase = TRUST_STORE_PASSWORD.toCharArray();
              KeyStore ts = KeyStore.getInstance("JKS");
              try (FileInputStream fis = new FileInputStream(TRUST_STORE_PATH)) {
                  ts.load(fis, passphrase);
              }

              TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
              tmf.init(ts);

              SSLContext sc = SSLContext.getInstance("TLS");
              sc.init(null, tmf.getTrustManagers(), null);

              SSLSocketFactory sf = sc.getSocketFactory();
              SSLSocket socket = (SSLSocket) sf.createSocket(HOST, PORT);
              socket.startHandshake(); // eksplicitno pokreni handshake
              
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               out = new PrintWriter(socket.getOutputStream(), true);
              //BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
              
              out.println(username);
              receiverThread = new Thread(() -> {
                  try {
                      String msg;
                      while ((msg = in.readLine()) != null) {
                          if (onMessage != null) onMessage.accept(msg);
                      }
                      log.info("Connection to secure chat - successfuly.");
                  } catch (IOException e) {
                      if (onMessage != null) onMessage.accept("[Veza prekinuta]");
                      log.severe(e.toString());
                  }
              });
              receiverThread.start();
              
    }
    


    public void sendMessage(String msg) {
        if (out != null) out.println(msg);
        log.info("Message sent.");
    }
    
    public void sendMessageMulticast(String group, String message) {
        if (out != null) {
            out.println("MULTICAST " + group + " " + message);
            log.info("Multicast message sent to group " + group);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            log.info("Disconnected from secure chat.");
        } catch (IOException ignored) {
        	log.severe(ignored.toString());
        }
    }

    // ove dvije metode će GUI klasa override-ovati
    protected void onMessageReceived(String msg) { }
    protected void onConnectionClosed() { }
	
}
