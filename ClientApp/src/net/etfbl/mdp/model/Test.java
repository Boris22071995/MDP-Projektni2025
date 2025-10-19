package net.etfbl.mdp.model;

import java.util.ArrayList;

import net.etfbl.mdp.service.ClientService;

public class Test {
	
	public static void main(String[] args) {
		ArrayList<Client> clients = ClientService.getClients();
		for(Client c : clients) {
			System.out.println(c);
		}
	}

}
