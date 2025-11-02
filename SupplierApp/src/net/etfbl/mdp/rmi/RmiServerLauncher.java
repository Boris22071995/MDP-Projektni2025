package net.etfbl.mdp.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServerLauncher {

	public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/InvoiceService", new InvoiceServiceImpl());
            System.out.println("[RMI Server] Running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
