package net.etfbl.mdp.securechat;
import javax.net.ssl.*;

import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

import java.io.*;
import java.security.KeyStore;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.File;

public class SSLChatClient {

	private static final Logger log = AppLogger.getLogger();
	
    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread receiverThread;
    
    private Consumer<String> onMessage;

    public SSLChatClient(Consumer<String> onMessage) {
        this.onMessage = onMessage;
    }
    
    public void connect(String username) throws Exception {
    	
    		  String HOST = ConfigurationLoader.getString("chat.host");
    		  int PORT = ConfigurationLoader.getInt("chat.port");
    		  String TRUST_STORE_PATH = ConfigurationLoader.getString("ssl.truststore.path");
    		  String TRUST_STORE_PASSWORD = ConfigurationLoader.getString("ssl.truststore.password");
    	
    	      File f = new File(TRUST_STORE_PATH);
    	      String trustStoreAbsPath = f.getAbsolutePath();
              System.setProperty("javax.net.ssl.trustStore", trustStoreAbsPath);
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
              socket.startHandshake();
              
              in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              out = new PrintWriter(socket.getOutputStream(), true);
              
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

}
