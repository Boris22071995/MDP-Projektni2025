package net.etfbl.mdp.securechat;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.io.FileInputStream;
import java.io.File;

public class SSLChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9443;
    private static String TRUST_STORE_PATH = "./truststore.jks";
    private static final String TRUST_STORE_PASSWORD = "servis123";

    public static void main(String[] args) {
        try {
            // Absolutna putanja do truststore-a
            File f = new File(TRUST_STORE_PATH);
            TRUST_STORE_PATH = f.getAbsolutePath();

            System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
            System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);

            // Učitavanje truststore-a
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

            System.out.println("[SSLChatClient] ✅ Connected securely to " + HOST + ":" + PORT);

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

            System.out.print("Unesite ime: ");
            String name = console.readLine();
            out.println(name);

            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                if ("exit".equalsIgnoreCase(line)) break;
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
