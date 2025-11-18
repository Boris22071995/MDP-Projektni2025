package net.etfbl.mdp.server;

public class ServerLauncher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread(new SupplierServer("Supplier1", "5001")).start();
        new Thread(new SupplierServer("Supplier2", "5002")).start();
        new Thread(new SupplierServer("Supplier3", "5003")).start();
        new Thread(new SupplierServer("Supplier4", "5004")).start();
	}

}
