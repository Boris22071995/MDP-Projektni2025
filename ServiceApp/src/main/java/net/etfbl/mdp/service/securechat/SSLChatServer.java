package net.etfbl.mdp.service.securechat;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import net.etfbl.mdp.service.securechat.storage.OfflineMessageStorage;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class SSLChatServer implements Runnable {

	private static final int PORT = ConfigurationLoader.getInt("ssl.port");
	private static String KEY_STORE_PATH = ConfigurationLoader.getString("keystore.path");
	private static final String KEY_STORE_PASSWORD = ConfigurationLoader.getString("keystore.password");
	private static final Set<SSLClientHandler> clients = new HashSet<>();
	private static Map<String, SSLClientHandler> onlineUsers = new HashMap<>();
	private static Map<String, Set<SSLClientHandler>> groups = new HashMap<>();
	private static final Logger log = AppLogger.getLogger();

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

		//	System.out.println("[SSLChatServer] Secure server started on port " + PORT);

			while (true) {
				SSLSocket socket = (SSLSocket) serverSocket.accept();
				SSLClientHandler handler = new SSLClientHandler(socket);
				clients.add(handler);
				new Thread(handler).start();
			}

		} catch (Exception e) {
			log.severe("Error with messaging");
			e.printStackTrace();
		}
	}

	public static synchronized void broadcast(String message, SSLClientHandler exclude) {
		for (SSLClientHandler client : clients) {
			if (client != exclude) {
				client.sendMessage(message);
			}
		}
	}

	public static synchronized void removeClient(SSLClientHandler client) {
		clients.remove(client);
		//System.out.println("[SSLChatServer] Removed client: " + client.getName());
	}

	public static synchronized void sendPrivate(String toUser, String msg) {
		SSLClientHandler recipient = onlineUsers.get(toUser);
		if (recipient != null) {
			recipient.sendMessage(msg);
		} else {
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

		if (!groups.containsKey(group))
			return;

		String formatted = "[Group " + group + "] " + fromUser + ": " + text;

		for (SSLClientHandler c : groups.get(group)) {
			if (c.getName().equals(fromUser))
				continue;

			if (onlineUsers.containsKey(c.getName())) {
				c.sendMessage(formatted);
			} else {
				OfflineMessageStorage.saveMessage(c.getName(), fromUser, formatted);
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
