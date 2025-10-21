package net.etfbl.mdp.model;

import net.etfbl.mdp.service.repository.ClientRepository;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientRepository repo = new ClientRepository();
		
		/*(String name, String lastName, String username, String password, String address, String email,
			String phone, String vehicleData, boolean approved, boolean blocked)*/
		
		Client c11 = new Client("korisnik1","korisnik1","korisnik1","korisnik1","korisnik1","korisnik1","korisnik1","korisnik1",true,false);
		repo.addClient(c11);
		
		Client c12 = new Client("korisnik2","korisnik2","korisnik2","korisnik2","korisnik2","korisnik2","korisnik2","korisnik2",true,false);
		repo.addClient(c12);
		
//		Client c11 = new Client("A1","A1","A1","A1","adrA1","emA1","phA1","AA1",false,false);
//		repo.addClient(c11);
//		Client c12 = new Client("A2","A2","A2","A2","adrA2","emA2","phA2","AA2",false,false);
//		repo.addClient(c12);
//		Client c13 = new Client("A3","A3","A3","A3","adrA3","emA3","phA3","AA3",false,false);
//		repo.addClient(c13);
//		Client c14 = new Client("A4","A4","A4","A4","adrA4","emA4","phA4","AA4",false,false);
//		repo.addClient(c14);
//		Client c15 = new Client("A5","A5","A5","A5","adrA5","emA5","phA5","AA5",false,false);
//		repo.addClient(c15);
//		System.out.println("Trenutni klijenti1:");
//		repo.getAllClients().forEach(System.out::println);
//		
//		
//		Client temp = repo.findByUsername("A1");
//		System.out.println("Klijent pronadjen: " + temp.toString());
//		
//	   c11.setApproved(true);
//	   c11.setBlocked(true);
//	   repo.updateClient(c11);	
//		System.out.println("Trenutni klijenti2:");
//		repo.getAllClients().forEach(System.out::println);
//		
//		repo.deleteClient("A5");
//		System.out.println("Trenutni klijenti3:");
//		repo.getAllClients().forEach(System.out::println);
	}

}
