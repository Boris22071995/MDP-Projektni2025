package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.service.securechat.SSLChatServer;
import javax.swing.*;
import java.awt.*;

public class ChatServerPanel extends JPanel {

	private JTextArea logArea;

	public ChatServerPanel() {
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel title = new JLabel("Chat Server Monitor", SwingConstants.CENTER);
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);

		logArea = new JTextArea();
		logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		logArea.setEditable(false);
		logArea.setBorder(BorderFactory.createTitledBorder("Server log"));

		add(new JScrollPane(logArea), BorderLayout.CENTER);

		JButton refreshButton = new JButton("Refresh online clients");
		refreshButton.setBackground(new Color(52, 73, 94));
		refreshButton.setForeground(Color.BLACK);
		refreshButton.setFocusPainted(false);
		refreshButton.addActionListener(e -> refreshUsers());
		add(refreshButton, BorderLayout.SOUTH);

		refreshUsers();

	}

	private void refreshUsers() {
		logArea.setText("Online clients:\n");
		for (String username : SSLChatServer.getOnlineUsers()) {
			logArea.append(" - " + username + "\n");
		}
	}

}
