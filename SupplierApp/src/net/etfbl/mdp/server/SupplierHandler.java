package net.etfbl.mdp.server;

import net.etfbl.mdp.model.Part;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SupplierHandler implements Runnable{
	
	private Socket socket;
	public SupplierHandler(Socket socket) {
		this.socket = socket;
	}
	
	  @Override
	    public void run() {
	        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
	             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

	            String command = (String) in.readObject();

	            switch (command) {
	                case "GET_PARTS":
	                    List<Part> list = SupplierServer.getParts();
	                    out.writeObject(list);
	                    break;

	                case "ADD_PART":
	                    Part newPart = (Part) in.readObject();
	                    SupplierServer.getParts().add(newPart);
	                    out.writeObject("OK");
	                    break;

	                // TODO: RMI slanje računa, obrada narudžbi
	                default:
	                    out.writeObject("UNKNOWN_COMMAND");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
