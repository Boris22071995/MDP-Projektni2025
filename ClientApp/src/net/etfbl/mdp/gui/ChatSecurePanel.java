package net.etfbl.mdp.gui;

import net.etfbl.mdp.securechat.SSLChatClientTest;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatSecurePanel extends JPanel {

	private static final Logger log = AppLogger.getLogger();
	
    private JTextArea chatArea;
    private JTextField inputField;

    private JList<String> userList;
    private JList<String> groupList;

    private DefaultListModel<String> groupsModel;

    private SSLChatClientTest chatClient;
    private String currentUser;

    public ChatSecurePanel(String currentUser, List<String> allUsers) {

        this.currentUser = currentUser;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        allUsers.removeIf(u -> u.equalsIgnoreCase(currentUser));

        userList = new JList<>(allUsers.toArray(new String[0]));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBorder(BorderFactory.createTitledBorder("Contacts"));

        groupsModel = new DefaultListModel<>();
        groupList = new JList<>(groupsModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBorder(BorderFactory.createTitledBorder("Groups"));

        // LEFT PANEL (users + groups)
        JPanel left = new JPanel(new GridLayout(2, 1, 5, 5));
        left.add(new JScrollPane(userList));
        left.add(new JScrollPane(groupList));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        chatArea.setBorder(BorderFactory.createTitledBorder("Messages"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                left,
                new JScrollPane(chatArea)
        );
        split.setDividerLocation(200);
        add(split, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createTitledBorder("Type a message..."));
        add(inputField, BorderLayout.SOUTH);

        
        chatClient = new SSLChatClientTest(
            msg -> SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"))
        );

        try {
            chatClient.connect(currentUser);
            log.info("User: " + currentUser + " connected to secure chat.");
            chatArea.append("Connected to secure chat.\n");
        } catch (Exception e) {
            chatArea.append("Connection error: " + e.getMessage() + "\n");
            log.warning("Connection to secure chat - error.");
            log.severe(e.toString());
        }

        
        inputField.addActionListener(e -> sendMessage());

        
        JPanel topButtons = new JPanel(new FlowLayout());
        JButton btnCreateGroup = new JButton("Create Group");
        JButton btnJoinGroup = new JButton("Join Group");
        JButton btnSendGroup = new JButton("Send to Group");
        JButton btnAddUserToGroup = new JButton("Add User to Group");
        btnAddUserToGroup.addActionListener(e -> addUserToGroup());
        topButtons.add(btnAddUserToGroup);
        

        btnCreateGroup.addActionListener(e -> createGroup());
        btnJoinGroup.addActionListener(e -> joinGroup());
        btnSendGroup.addActionListener(e -> sendGroupMessage());

        topButtons.add(btnCreateGroup);
        topButtons.add(btnJoinGroup);
        topButtons.add(btnSendGroup);

        add(topButtons, BorderLayout.NORTH);
    }
    
    private void addUserToGroup() {
        String selectedGroup = groupList.getSelectedValue();
        String selectedUser = userList.getSelectedValue();

        if (selectedGroup == null) {
        	log.warning("Error - select group first.");
            JOptionPane.showMessageDialog(this, "Select a group first.");
            return;
        }

        if (selectedUser == null) {
        	log.warning("Error - select user to add.");
            JOptionPane.showMessageDialog(this, "Select a user to add.");
            return;
        }

        chatClient.sendMessage("ADD_TO_GROUP " + selectedGroup + " " + selectedUser);
        log.info(currentUser + " added " + selectedUser + " to a group " + selectedGroup + ".");
        chatArea.append("You added " + selectedUser + " to group " + selectedGroup + ".\n");
    }

    private void sendMessage() {
        String to = userList.getSelectedValue();
        String msg = inputField.getText().trim();

        if (to == null || msg.isEmpty()) return;

        chatClient.sendMessage("@" + to + ": " + msg);
        log.info("Message sent to: " + to);
        chatArea.append("[You -> " + to + "]: " + msg + "\n");
        inputField.setText("");
    }

    private void sendGroupMessage() {
        String group = groupList.getSelectedValue();
        String msg = inputField.getText().trim();

        if (group == null || msg.isEmpty()) return;

        //chatClient.sendMessage("#" + group + ": " + msg);
        chatClient.sendMessageMulticast(group, msg);
        log.info("Message sent to group: " + group);
        chatArea.append("[You -> " + group + "]: " + msg + "\n");
        inputField.setText("");
    }

    private void createGroup() {
        String name = JOptionPane.showInputDialog(this, "Group name:");
        if (name == null || name.trim().isEmpty()) return;

        name = name.trim();

        if (!groupsModel.contains(name))
            groupsModel.addElement(name);

       
        chatClient.sendMessage("CREATE_GROUP " + name);
        log.info("Group is created: " + name);
        chatArea.append("You created group: " + name + "\n");
    }

    private void joinGroup() {
        String group = groupList.getSelectedValue();
        if (group == null) return;

        chatClient.sendMessage("JOIN_GROUP " + group);
        log.info("User joined to group: " + group);
        chatArea.append("You joined group: " + group + "\n");
    }
    
}

