package net.etfbl.mdp.securechat;


import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.io.FileInputStream;
import java.io.File;

public class SSLChatClientTest {


    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread receiverThread;
    
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
              
              BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
              BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

              // Thread za primanje poruka
              new Thread(() -> {
                  try {
                      String msg;
                      while ((msg = in.readLine()) != null) {
                          System.out.println(msg);
                      }
                  } catch (IOException e) {
                      System.out.println("Veza prekinuta.");
                  }
              }).start();
    }
    

   /* public void connect(String username) throws Exception {
        File tsFile = new File("truststore.jks");
        if (!tsFile.exists())
            throw new FileNotFoundException("Truststore nije pronađen: " + tsFile.getAbsolutePath());

        System.setProperty("javax.net.ssl.trustStore", tsFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", "servis123");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) sf.createSocket("localhost", 9443);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // šalje korisničko ime serveru
        out.println(username);

        // nit koja stalno sluša nove poruke
        receiverThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    onMessageReceived(msg);
                }
            } catch (IOException e) {
                onConnectionClosed();
            }
        });
        receiverThread.start();
    }*/

    public void sendMessage(String msg) {
        if (out != null) out.println(msg);
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) { }
    }

    // ove dvije metode će GUI klasa override-ovati
    protected void onMessageReceived(String msg) { }
    protected void onConnectionClosed() { }
	
}
