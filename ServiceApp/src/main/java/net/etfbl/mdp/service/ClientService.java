package net.etfbl.mdp.service;

import java.util.ArrayList;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.repository.ClientRepository;

public class ClientService {

	private ClientRepository repository = new ClientRepository();
	
	public ArrayList<Client> getAllClients(){
		return repository.getAllClients();
	}
	
	public Client findByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	public boolean register(Client c) {
		if(repository.findByUsername(c.getUsername()) != null)
			return false;
		repository.addClient(c);
		return true;
	}
	
	public boolean login(String username, String password) {
		Client c = repository.findByUsername(username);
		return (c != null && c.getPassword().equals(password) && c.isApproved() && !c.isBlocked());
	}
	
	public void approveClient(String username) {
		Client c = repository.findByUsername(username);
		if(c != null) {
			c.setApproved(true);
			repository.updateClient(c);
		}
	}
	
	public void blockClient(String username) {
		Client c = repository.findByUsername(username);
		if(c != null) {
			c.setBlocked(true);
			repository.updateClient(c);
		}
	}
	
}
