package net.etfbl.mdp.gui;

import net.etfbl.mdp.securechat.SSLChatClientTest;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatSecurePanel extends JPanel {

    private JTextArea chatArea;
    private JTextField inputField;
    private JComboBox<String> userList;
    private SSLChatClientTest chatClient;
    private String currentUser;

    public ChatSecurePanel(String currentUser, List<String> allUsers) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());

        // Filter - izbacimo samog sebe
        allUsers.removeIf(u -> u.equalsIgnoreCase(currentUser));

        userList = new JComboBox<>(allUsers.toArray(new String[0]));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Pošalji korisniku:"));
        top.add(userList);
        add(top, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);
        
        connectSecureChat();
        
        inputField.addActionListener(e -> {
            String to = (String) userList.getSelectedItem();
            String msg = inputField.getText().trim();
            if (!msg.isEmpty() && to != null) {
                chatClient.sendMessage("@" + to + ": " + msg);
                chatArea.append("[Ti -> " + to + "]: " + msg + "\n");
                inputField.setText("");
            }
        });

        
    }

    private void connectSecureChat() {
        chatClient = new SSLChatClientTest(msg ->
                SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n")));

        try {
            chatClient.connect(currentUser);
            chatArea.append("✅ Povezan na sigurni chat.\n");
        } catch (Exception e) {
            chatArea.append("❌ Greška pri konekciji: " + e.getMessage() + "\n");
        }
    }
}
