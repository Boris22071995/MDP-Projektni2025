package net.etfbl.mdp.service.securechat;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import net.etfbl.mdp.service.securechat.storage.OfflineMessageStorage;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SSLChatServer implements Runnable{

    private static final int PORT = 9443;
    private static String KEY_STORE_PATH = "./keystore.jks";
    private static final String KEY_STORE_PASSWORD = "servis123";
    private static final Set<SSLClientHandler> clients = new HashSet<>();
    private static Map<String, SSLClientHandler> onlineUsers = new HashMap<>();
    private static Map<String, Set<SSLClientHandler>> groups = new HashMap<>();
    
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

             System.out.println("[SSLChatServer] ✅ Secure server started on port " + PORT);

             while (true) {
                 SSLSocket socket = (SSLSocket) serverSocket.accept();
                 SSLClientHandler handler = new SSLClientHandler(socket);
                 clients.add(handler);
                 new Thread(handler).start();
             }

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    

//    public static void main(String[] args) {
//        try {
//            // Absolutna putanja do keystore-a
//            File f = new File(KEY_STORE_PATH);
//            KEY_STORE_PATH = f.getAbsolutePath();
//
//            System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
//            System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);
//
//            // Učitavanje keystore-a
//            char[] passphrase = KEY_STORE_PASSWORD.toCharArray();
//            KeyStore ks = KeyStore.getInstance("JKS");
//            try (FileInputStream fis = new FileInputStream(KEY_STORE_PATH)) {
//                ks.load(fis, passphrase);
//            }
//
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//            kmf.init(ks, passphrase);
//
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(kmf.getKeyManagers(), null, null);
//
//            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
//            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
//
//            System.out.println("[SSLChatServer] ✅ Secure server started on port " + PORT);
//
//            while (true) {
//                SSLSocket socket = (SSLSocket) serverSocket.accept();
//                SSLClientHandler handler = new SSLClientHandler(socket);
//                clients.add(handler);
//                new Thread(handler).start();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static synchronized void broadcast(String message, SSLClientHandler exclude) {
        for (SSLClientHandler client : clients) {
            if (client != exclude) {
                client.sendMessage(message);
            }
        }
    }

    public static synchronized void removeClient(SSLClientHandler client) {
        clients.remove(client);
        System.out.println("[SSLChatServer] Removed client: " + client.getName());
    }
    
    /*public static synchronized void sendPrivate(String username, String msg) {
        for (SSLClientHandler c : clients) {
            if (username.equalsIgnoreCase(c.getName())) {
                c.sendMessage(msg);
                break;
            }
        }
    }*/
    
    public static synchronized void sendPrivate(String toUser, String msg) {
        SSLClientHandler recipient = onlineUsers.get(toUser);
        if (recipient != null) {
            recipient.sendMessage(msg);
        } else {
            // offline korisnik - čuvaj poruku
            OfflineMessageStorage.saveMessage(toUser, "Server", msg);
        }
    }
    
    
    public static synchronized void createGroup(String name, SSLClientHandler creator) {
        groups.putIfAbsent(name, new HashSet<>());
        groups.get(name).add(creator);
        creator.sendMessage("[Server] Kreirana grupa: " + name);
    }

    public static synchronized void joinGroup(String name, SSLClientHandler user) {
        groups.putIfAbsent(name, new HashSet<>());
        groups.get(name).add(user);
        user.sendMessage("[Server] Pridruženi ste grupi: " + name);
    }

    public static synchronized void addUserToGroup(String group, String username) {
        SSLClientHandler c = onlineUsers.get(username);
        if (c != null) {
            groups.putIfAbsent(group, new HashSet<>());
            groups.get(group).add(c);
            c.sendMessage("[Server] Dodani ste u grupu: " + group);
        }
    }

    public static synchronized void sendMulticast(String group, String fromUser, String text) {
    	
        if (!groups.containsKey(group)) return;

        String formatted = "[Group " + group + "] " + fromUser + ": " + text;

        for (SSLClientHandler c : groups.get(group)) {
        	System.out.println("DA LI SMO U SEND MULTICASTU e sad me ovo zanima");
            // preskoci samog sebe
            if (c.getName().equals(fromUser)) continue;

            // ako je online — šalji normalno
            if (onlineUsers.containsKey(c.getName())) {
                c.sendMessage(formatted);
            } 
            else {
            	System.out.println("DA LI SMO OVDJE");
                // ako je OFFLINE — spremi poruku
                OfflineMessageStorage.saveMessage(
                    c.getName(),        // korisnik za kojeg se čuva poruka
                    fromUser,           // ko je poslao
                    formatted           // sadržaj poruke
                );
            }
        }
    }


    
    
    public static synchronized void registerUser(String username, SSLClientHandler handler) {
        onlineUsers.put(username, handler);
    }
    
    public static synchronized void unregisterUser(String username) {
        onlineUsers.remove(username);
    }
    
    public static synchronized boolean isOnline(String username) {
        return onlineUsers.containsKey(username);
    }
    
    public static synchronized Set<String> getOnlineUsers() {
        return onlineUsers.keySet();
    }
}
