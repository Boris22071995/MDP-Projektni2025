package net.etfbl.mdp.service.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.util.AppLogger;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

public class ClientRepository {

	private static final String FILE_PATH = "C:\\Users\\Boris\\OneDrive\\Desktop\\MDP-Projektni2025\\ServiceApp\\webapp\\WEB-INF\\clients.xml";
	private static final Logger log = AppLogger.getLogger();

	@SuppressWarnings("unchecked")
	public ArrayList<Client> getAllClients() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			log.severe("File doesn't exist.");
			return new ArrayList<>();
		}

		try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)))) {
			List<Client> clientList = (List<Client>) decoder.readObject();
			return new ArrayList<>(clientList);
		} catch (Exception e) {
			log.severe("Error while retriving clients from file.");
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void saveAllClients(List<Client> clients) {
		try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_PATH)))) {
			encoder.writeObject(clients);
		} catch (Exception e) {
			log.severe("Error while saving client.");
			e.printStackTrace();
		}
	}

	public Client findByUsername(String username) {
		for (Client c : getAllClients()) {
			if (c.getUsername().equalsIgnoreCase(username))
				return c;
		}
		return null;
	}

	public void addClient(Client client) {
		List<Client> clients = getAllClients();
		clients.add(client);
		saveAllClients(clients);

	}

	public void updateClient(Client client) {
		List<Client> clients = getAllClients();
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equalsIgnoreCase(client.getUsername())) {
				clients.set(i, client);
				break;
			}
		}
		saveAllClients(clients);
	}

	public void deleteClient(String username) {
		List<Client> clients = getAllClients();
		clients.removeIf(c -> c.getUsername().equalsIgnoreCase(username));
		saveAllClients(clients);
	}

}
