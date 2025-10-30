package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.ClientService;

import javax.swing.*;
import java.awt.*;

import java.util.*;
import java.util.List;

public class MainFrame extends JFrame{
	Client client;
	private CardLayout cardLayout;
	private JPanel mainPanel;

	public MainFrame(Client client) {
		
		this.client = client;
        setTitle("Welcome, " + client.getName() + " " + client.getLastName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // dodajemo panele
        ReservationPanel reservationPanel = new ReservationPanel(client);
        HistoryPanel historyPanel = new HistoryPanel(client);
        mainPanel.add(reservationPanel, "reservation");
        mainPanel.add(historyPanel, "history");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton btnSchedule = new JButton("Termin reservation");
        JButton btnHistory = new JButton("Service history");
        JButton btnChatClient = new JButton("Chat");
        JButton btnChatService = new JButton("Chat with service");

        // akcije
        btnSchedule.addActionListener(e -> cardLayout.show(mainPanel, "reservation"));
        btnHistory.addActionListener(e -> cardLayout.show(mainPanel, "history"));

        btnChatClient.addActionListener(e -> {
            List<Client> users = ClientService.getClients();
            List<String> imena = new ArrayList<>();
            for (Client c : users) imena.add(c.getUsername());
            JFrame chatFrame = new JFrame("Sigurni chat - " + client.getUsername());
            chatFrame.add(new ChatSecurePanel(client.getUsername(), imena));
            chatFrame.setSize(600, 400);
            chatFrame.setVisible(true);
        });

        buttonPanel.add(btnSchedule);
        buttonPanel.add(btnHistory);
        buttonPanel.add(btnChatService);
        buttonPanel.add(btnChatClient);

        add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
		
//		this.client = client;
//		setTitle("Welcome, " + client.getName() + " " + client.getLastName());
//        setSize(500, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(4, 1, 10, 10));
//
//        JButton btnSchedule = new JButton("Termin reservation");
//        JButton btnHistory = new JButton("Service history");
//        JButton btnChatClient = new JButton("Chat");
//        JButton btnChatService = new JButton("Chat with service");
//
//        add(btnSchedule);
//        add(btnHistory);
//        add(btnChatService);
//        btnChatClient.addActionListener(e -> {
//            List<Client> users = ClientService.getClients();
//            List<String> imena = new ArrayList<>();
//            for(Client c : users) imena.add(c.getUsername());
//            JFrame chatFrame = new JFrame("Sigurni chat - " + client.getUsername());
//            chatFrame.add(new ChatSecurePanel(client.getUsername(), imena));
//            chatFrame.setSize(600, 400);
//            chatFrame.setVisible(true);
//        });
//        add(btnChatClient);
  
       
        /*ArrayList<Client> korisnici = ClientService.getClients(); // GET /clients/all
        System.out.println("OVDJE SMO: " + korisnici.size());
        List<String> imena = new ArrayList<>();
        for(Client c : korisnici) imena.add(c.getUsername());
        JFrame chatFrame = new JFrame("Sigurni chat");
        chatFrame.add(new ChatSecurePanel(client.getName(), imena));
        chatFrame.setSize(500, 400);
        chatFrame.setVisible(true);*/
	 private void openChatClient() {
		 ArrayList<Client> korisnici = ClientService.getClients();
	        List<String> imena = new ArrayList<>();
	        for(Client c : korisnici) imena.add(c.getUsername());
		 new ChatSecurePanel(client.getUsername(),imena).setVisible(true);;
			this.dispose();
     }
}

