package net.etfbl.mdp.gui;

import net.etfbl.mdp.securechat.SSLChatClientTest;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ChatServicePanel extends JPanel{
	
	  private static final Logger log = AppLogger.getLogger();

	  private JTextArea chatArea;
	  private JTextField inputField;

	  private SSLChatClientTest chatClient;
	  private String currentUser;

	  private static final String SERVICE_USERNAME = "service"; 
	  
	  public ChatServicePanel(String currentUser) {

		  this.currentUser = currentUser;

	        setLayout(new BorderLayout(10, 10));
	        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        setBackground(Color.WHITE);

	        // CHAT AREA (kao u ChatSecurePanel)
	        chatArea = new JTextArea();
	        chatArea.setEditable(false);
	        chatArea.setFont(new Font("Consolas", Font.PLAIN, 13));
	        chatArea.setBorder(BorderFactory.createTitledBorder("Chat with Service"));
	        add(new JScrollPane(chatArea), BorderLayout.CENTER);

	        // INPUT
	        inputField = new JTextField();
	        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        inputField.setBorder(BorderFactory.createTitledBorder("Type a message..."));
	        add(inputField, BorderLayout.SOUTH);

	        // CLIENT INIT
	        chatClient = new SSLChatClientTest(
	                msg -> SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"))
	        );

	        try {
	            chatClient.connect(currentUser);
	            chatArea.append("Connected to service chat.\n");
	            log.info(currentUser + " connected to service chat panel.");
	        } catch (Exception e) {
	            chatArea.append("Connection error: " + e.getMessage() + "\n");
	            log.severe("Service chat connection error: " + e);
	        }

	        // SEND ACTION
	        inputField.addActionListener(e -> sendMessage());
	    }
	  
	  private void sendMessage() {
	        String msg = inputField.getText().trim();
	        if (msg.isEmpty()) return;

	        // Format poruke prema serviseru
	        chatClient.sendMessage("#" + SERVICE_USERNAME + ": " + msg);

	        chatArea.append("[You -> Service]: " + msg + "\n");
	        inputField.setText("");

	        log.info(currentUser + " sent to service: " + msg);
	    }
	
}
