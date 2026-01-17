package net.etfbl.mdp.service;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.repository.ClientRepository;
import net.etfbl.mdp.util.AppLogger;

public class ClientService {

	private ClientRepository repository = new ClientRepository();
	private static final Logger log = AppLogger.getLogger();

	public ArrayList<Client> getAllClients() {
		log.info("Clients are retrived.");
		return repository.getAllClients();
	}

	public Client findByUsername(String username) {
		return repository.findByUsername(username);
	}

	public boolean register(Client c) {
		if (repository.findByUsername(c.getUsername()) != null)
			return false;
		repository.addClient(c);
		log.info("Client is registered.");
		return true;
	}

	public boolean login(String username, String password) {
		Client c = repository.findByUsername(username);
		return (c != null && c.getPassword().equals(password));
	}

	public void approveClient(String username) {
		Client c = repository.findByUsername(username);
		if (c != null) {
			c.setApproved(true);
			repository.updateClient(c);
		}
	}

	public void blockClient(String username) {
		Client c = repository.findByUsername(username);
		if (c != null) {
			c.setBlocked(true);
			repository.updateClient(c);
		}
	}

	public void unblockClient(String username) {
		Client c = repository.findByUsername(username);
		if (c != null) {
			c.setBlocked(false);
			repository.updateClient(c);
		}
	}

	public void delete(String username) {
		repository.deleteClient(username);
	}

}
