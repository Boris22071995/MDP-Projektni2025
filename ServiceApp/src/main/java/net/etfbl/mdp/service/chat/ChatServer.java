package net.etfbl.mdp.service.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

	private static final int PORT = 9000;
	private static Set<ClientHandler> clients = new HashSet<>();
	
	public static void main(String[] args) {
		System.out.println("[Server] Chat server started at port " + PORT);
		
		try(ServerSocket serverSocket = new ServerSocket(PORT)) {
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("[Server] New client connected: " + socket.getInetAddress());
				ClientHandler handler = new ClientHandler(socket);
				clients.add(handler);
				new Thread(handler).start();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void broadcast(String message, ClientHandler excludeUser) {
		for(ClientHandler client : clients) {
			if(client != excludeUser) {
				client.sendMessage(message);
			}
		}
	}
	
	public static synchronized void removeClient(ClientHandler client) {
		clients.remove(client);
		System.out.println("[Server] Client removed: " + client.getName());
	}
	
}
