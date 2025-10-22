package net.etfbl.mdp.chat;

import java.io.*;
import java.net.*;

public class ClientChat {

	 private static final String SERVER_ADDRESS = "localhost";
	 private static final int SERVER_PORT = 9000;
	 
	 public static void main(String[] args) {
		 try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

	            // Thread koji prima poruke
	            new Thread(() -> {
	                String msg;
	                try {
	                    while ((msg = in.readLine()) != null) {
	                        System.out.println(msg);
	                    }
	                } catch (IOException e) {
	                    System.out.println("Connection interrupted.");
	                }
	            }).start();

	            // Slanje poruka
	            System.out.print("Unesite ime: ");
	            String name = console.readLine();
	            out.println(name);

	            String message;
	            while ((message = console.readLine()) != null) {
	                out.println(message);
	                if ("exit".equalsIgnoreCase(message)) break;
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
				 
	 }
	
	
	
}
