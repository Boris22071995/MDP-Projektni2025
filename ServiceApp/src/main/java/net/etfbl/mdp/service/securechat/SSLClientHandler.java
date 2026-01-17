package net.etfbl.mdp.service.securechat;

import javax.net.ssl.SSLSocket;

import net.etfbl.mdp.service.securechat.storage.OfflineMessageStorage;
import net.etfbl.mdp.util.AppLogger;

import java.io.*;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Logger;

public class SSLClientHandler implements Runnable {

	private SSLSocket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String name;
	private static final Logger log = AppLogger.getLogger();

	public SSLClientHandler(SSLSocket socket) {
		this.socket = socket;
	}

	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			name = in.readLine();
			SSLChatServer.registerUser(name, this);
			SSLChatServer.broadcast(name + " se priključio sigurnom chatu.", this);

			List<String> offlineMsgs = OfflineMessageStorage.loadMessagesForUser(name);
			for (String m : offlineMsgs) {
				sendMessage(m);
			}

			String message;

			while ((message = in.readLine()) != null) {

				if ("exit".equalsIgnoreCase(message))
					break;

				if (message.startsWith("CREATE_GROUP")) {
					String group = message.split(" ")[1];
					SSLChatServer.createGroup(group, this);
					continue;
				}

				if (message.startsWith("JOIN_GROUP")) {
					String group = message.split(" ")[1];
					SSLChatServer.joinGroup(group, this);
					continue;
				}

				if (message.startsWith("ADD_TO_GROUP")) {
					String[] parts = message.split(" ");
					String group = parts[1];
					String user = parts[2];
					SSLChatServer.addUserToGroup(group, user);
					continue;
				}

				if (message.startsWith("MULTICAST")) {
					String[] parts = message.split(" ", 3);
					String group = parts[1];
					String content = parts[2];
					SSLChatServer.sendMulticast(group, name, content);
					continue;
				}

				if (message.startsWith("@")) {
					int sep = message.indexOf(":");
					if (sep > 1) {
						String toUser = message.substring(1, sep).trim();
						String content = message.substring(sep + 1).trim();
						SSLChatServer.sendPrivate(toUser, name + ": " + content);
					}
					continue;
				}

				if (message.startsWith("#")) {
					int sep = message.indexOf(":");
					if (sep > 1) {
						String content = message.substring(sep + 1).trim();
						ServerEvents.messageReceived(name, content);
					}
				}

				SSLChatServer.broadcast(name + ": " + message, this);
			}

		} catch (SocketException e1) {
			log.severe("Error with socket.");
		} catch (IOException e) {
			log.severe("Error while sending message");
			e.printStackTrace();
		} finally {
			SSLChatServer.unregisterUser(name);
			SSLChatServer.removeClient(this);
			SSLChatServer.broadcast(name + " je napustio chat.", this);

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

}
