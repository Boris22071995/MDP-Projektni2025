package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.ClientService;

import javax.swing.*;
import java.awt.*;

import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
	Client client;
	private CardLayout cardLayout;
	private JPanel mainPanel;

	public MainFrame(Client client) {

		this.client = client;
		setTitle("Service Portal - " + client.getUsername());
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JLabel lblHeader = new JLabel("Car Service Portal", SwingConstants.CENTER);
		lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(lblHeader, BorderLayout.NORTH);

		JPanel sidebar = new JPanel(new GridLayout(7, 1, 10, 10));
		sidebar.setBackground(new Color(33, 47, 61));
		sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

		JButton btnReservation = createSidebarButton("Reservation");
		JButton btnHistory = createSidebarButton("Reservation history");
		JButton btnChat = createSidebarButton("Chat");
		JButton btnChatService = createSidebarButton("Chat with service");
		JButton btnLogout = createSidebarButton("Log out");

		sidebar.add(btnReservation);
		sidebar.add(btnHistory);
		sidebar.add(btnChat);
		sidebar.add(btnChatService);
		sidebar.add(new JLabel());
		sidebar.add(btnLogout);

		add(sidebar, BorderLayout.WEST);

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		ReservationPanel reservationPanel = new ReservationPanel(client);
		HistoryPanel historyPanel = new HistoryPanel(client);

		mainPanel.add(reservationPanel, "reservation");
		mainPanel.add(historyPanel, "history");

		add(mainPanel, BorderLayout.CENTER);

		btnReservation.addActionListener(e -> cardLayout.show(mainPanel, "reservation"));
		btnHistory.addActionListener(e -> cardLayout.show(mainPanel, "history"));

		btnChat.addActionListener(e -> openChatClient());
		btnChatService.addActionListener(e -> JOptionPane.showMessageDialog(this, "Not connected yet."));
		btnLogout.addActionListener(e -> logout());

		setVisible(true);

	}

	private void openChatClient() {
		List<Client> users = ClientService.getClients();
		List<String> names = new ArrayList<>();
		for (Client c : users)
			names.add(c.getUsername());

		JFrame chatFrame = new JFrame("Secure Chat - " + client.getUsername());
		chatFrame.add(new ChatSecurePanel(client.getUsername(), names));
		chatFrame.setSize(600, 450);
		chatFrame.setLocationRelativeTo(this);
		chatFrame.setVisible(true);
	}

	private JButton createSidebarButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(52, 73, 94));
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return button;
	}

	private void logout() {
		dispose();
		new LoginFrame().setVisible(true);
	}
}
