package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.service.ClientService;
import net.etfbl.mdp.service.securechat.SSLChatServer;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.securechat.ServerEvents;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatServerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();
	private JTextArea chatArea;
	private JTextField inputField;
	private JList<String> userList;
	ArrayList<Client> allUsers = new ArrayList<>();
	ClientService cs = new ClientService();

	public ChatServerPanel() {
		allUsers = cs.getAllClients();
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		List<String> userNames = new ArrayList<>();

		for (Client c : allUsers) {
			userNames.add(c.getUsername());
		}

		userList = new JList<>(userNames.toArray(new String[0]));
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBorder(BorderFactory.createTitledBorder("Clients"));

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

		JPanel placeholder = new JPanel();
		placeholder.setPreferredSize(new Dimension(0, 40));
		leftPanel.add(placeholder, BorderLayout.SOUTH);

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		chatArea.setBorder(BorderFactory.createTitledBorder("Messages"));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, new JScrollPane(chatArea));
		split.setDividerLocation(120);
		add(split, BorderLayout.CENTER);

		inputField = new JTextField();
		inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		inputField.setBorder(BorderFactory.createTitledBorder("Type a message..."));
		inputField.addActionListener(e -> sendMessage());
		add(inputField, BorderLayout.SOUTH);
	}

	private void sendMessage() {
		String to = userList.getSelectedValue();
		String msg = inputField.getText().trim();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String sendTime = LocalDateTime.now().format(formatter);      
		msg = msg + " time:" + sendTime;

		if (to == null || msg.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select a client first!");
			return;
		}

		SSLChatServer.sendPrivate(to, "[Service]: " + msg);
		chatArea.append("[YOU->" + to + "]: " + msg + "\n");
		inputField.setText("");
		log.info("Message from service is sent.");
	}

	public void registerListener() {
		ServerEvents.setListener(
				(from, msg) -> SwingUtilities.invokeLater(() -> chatArea.append(from + ": " + msg + "\n")));
	}

}
