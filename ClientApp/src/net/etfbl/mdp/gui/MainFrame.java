package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.ClientService;

import javax.swing.*;
import java.awt.*;

import java.util.*;
import java.util.List;

public class MainFrame extends JFrame{

	public MainFrame(Client client) {
		setTitle("Welcome, " + client.getName() + " " + client.getLastName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnSchedule = new JButton("Termin reservation");
        JButton btnHistory = new JButton("Service history");
        JButton btnChatClient = new JButton("Chat");
        JButton btnChatService = new JButton("Chat with service");

        add(btnSchedule);
        add(btnHistory);
        add(btnChatClient);
        add(btnChatService);
       
        ArrayList<Client> korisnici = ClientService.getClients(); // GET /clients/all
        System.out.println("OVDJE SMO: " + korisnici.size());
        List<String> imena = new ArrayList<>();
        for(Client c : korisnici) imena.add(c.getUsername());
        JFrame chatFrame = new JFrame("Sigurni chat");
        chatFrame.add(new ChatSecurePanel(client.getName(), imena));
        chatFrame.setSize(500, 400);
        chatFrame.setVisible(true);
        
	}
	
}
