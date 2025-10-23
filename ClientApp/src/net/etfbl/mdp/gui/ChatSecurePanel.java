package net.etfbl.mdp.gui;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import net.etfbl.mdp.securechat.SSLChatClientTest;

public class ChatSecurePanel extends JPanel {

	 private JTextArea chatArea;
	    private JTextField inputField;
	    private JComboBox<String> userList;
	    private SSLChatClientTest chatClient;
	    private String currentUser;

	    public ChatSecurePanel(String currentUser, List<String> allUsers) {
	        this.currentUser = currentUser;
	        setLayout(new BorderLayout());

	        // gornji panel sa listom korisnika
	        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        userList = new JComboBox<>(allUsers.toArray(new String[0]));
	        topPanel.add(new JLabel("Pošalji korisniku:"));
	        topPanel.add(userList);
	        add(topPanel, BorderLayout.NORTH);

	        // chat zona
	        chatArea = new JTextArea();
	        chatArea.setEditable(false);
	        add(new JScrollPane(chatArea), BorderLayout.CENTER);

	        // polje za unos
	        inputField = new JTextField();
	        add(inputField, BorderLayout.SOUTH);

	        inputField.addActionListener(e -> {
	            String toUser = (String) userList.getSelectedItem();
	            String msg = inputField.getText().trim();
	            if (!msg.isEmpty()) {
	                sendPrivateMessage(toUser, msg);
	                inputField.setText("");
	            }
	        });

	        // konekcija na SSL server
	        connectToSecureChat();
	    }

	    private void connectToSecureChat() {
	        chatClient = new SSLChatClientTest() {
	            @Override
	            protected void onMessageReceived(String msg) {
	                SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
	            }

	            @Override
	            protected void onConnectionClosed() {
	                SwingUtilities.invokeLater(() ->
	                        chatArea.append("[Veza sa serverom prekinuta]\n"));
	            }
	        };

	        try {
	            chatClient.connect(currentUser);
	            chatArea.append("✅ Povezan na sigurni chat.\n");
	        } catch (Exception e) {
	            chatArea.append("❌ Greška pri konekciji: " + e.getMessage() + "\n");
	        }
	    }

	    private void sendPrivateMessage(String toUser, String msg) {
	        String formatted = "@" + toUser + ": " + msg;
	        chatClient.sendMessage(formatted);
	        chatArea.append("[Ti -> " + toUser + "]: " + msg + "\n");
	    }
	
}
