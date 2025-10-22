package net.etfbl.mdp.service.chat;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String name;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			
			out.println("Welcome on chat server! Put your name:");
			name = in.readLine();
			System.out.println("[Server] User joined: " + name);
			ChatServer.broadcast(name + " is on chat.", this);
			
			String message;
			while((message = in.readLine()) != null) {
				if("exit".equalsIgnoreCase(message)) break;
				ChatServer.broadcast(name + ": " + message, this);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			ChatServer.removeClient(this);
			ChatServer.broadcast(name + " is out of chat.", this);
			try {
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(String message) {
		out.println(message);
	}

}
