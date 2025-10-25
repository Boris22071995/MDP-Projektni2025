package net.etfbl.mdp.server;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.parser.PartScraper;

import java.io.*;
import java.net.*;
import java.util.*;

public class SupplierServer {
	
	public static final int PORT = 9100;
	private static List<Part> parts = new ArrayList<>();
	
	public static void main(String[] args) {
		try {
			parts = PartScraper.fetchParts();
			System.out.println("[SupplierServer] Ucitano dijelova: " + parts.size());
			
			try(ServerSocket serverSocket = new ServerSocket(PORT)) {
				System.out.println("[SupplierServer] Server ceka na portu " + PORT);
				while(true) {
					Socket socket = serverSocket.accept();
					new Thread(new SupplierHandler(socket)).start();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Part> getParts() {
		return parts;
	}

}
