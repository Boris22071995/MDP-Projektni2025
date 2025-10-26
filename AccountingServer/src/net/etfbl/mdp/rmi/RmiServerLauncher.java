package net.etfbl.mdp.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServerLauncher {

	public static void main(String[] args) {
		try {
			System.setProperty("java.rmi.server.hostname", "localhost");
			LocateRegistry.createRegistry(1099);
			
			InvoiceService service = new InvoiceServiceImpl();
			Naming.rebind("rmi://localhost:1099/InvoiceService", service);
			
			System.out.println("[RMI Server] Knjigovodstveni servis pokrenut.");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
