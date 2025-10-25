package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.service.securechat.SSLChatServer;
import javax.swing.*;
import java.awt.*;

public class ChatServerPanel extends JPanel {

	private JTextArea logArea;
	
	public ChatServerPanel() {
        setLayout(new BorderLayout());
        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Osvježi online korisnike");
        refreshButton.addActionListener(e -> refreshUsers());

        add(refreshButton, BorderLayout.SOUTH);
    }
	
	private void refreshUsers() {
        logArea.setText("📶 Online korisnici:\n");
        for (String username : SSLChatServer.getOnlineUsers()) {
            logArea.append(" - " + username + "\n");
        }
    }
	
}
