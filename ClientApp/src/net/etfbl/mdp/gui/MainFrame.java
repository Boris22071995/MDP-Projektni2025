package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Client;

import javax.swing.*;
import java.awt.*;

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
	}
	
}
