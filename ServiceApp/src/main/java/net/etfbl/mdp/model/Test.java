package net.etfbl.mdp.model;

import net.etfbl.mdp.service.repository.ClientRepository;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientRepository repo = new ClientRepository();
		
		Client c1 = new Client("Boris", "Vasiljevic", "boris", "boris", "osjecani", "bv@mai.com", "066 032 089", "golf6", true, false);
		repo.addClient(c1);
		Client c2 = new Client("Boris2", "Vasiljevic2", "boris2", "boris2", "osjecani2", "bv@mai.com2", "066 032 089", "golf6", true, false);
		repo.addClient(c2);
		System.out.println("Trenutni klijenti:");
		repo.getAllClients().forEach(System.out::println);
	}

}
