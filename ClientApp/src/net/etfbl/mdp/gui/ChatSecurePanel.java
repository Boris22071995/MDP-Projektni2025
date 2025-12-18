package net.etfbl.mdp.gui;

import net.etfbl.mdp.securechat.SSLChatClient;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class ChatSecurePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = AppLogger.getLogger();	
    private JTextArea chatArea;
    private JTextField inputField;
    private JList<String> userList;
    private JList<String> groupList;
    private DefaultListModel<String> groupsModel;
    private SSLChatClient chatClient;
    private String currentUser;

    public ChatSecurePanel(String currentUser, List<String> allUsers) {

        this.currentUser = currentUser;
        String serviceName = "Service";
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        allUsers.removeIf(u -> u.equalsIgnoreCase(currentUser));
        allUsers.add(serviceName);
        
        userList = new JList<>(allUsers.toArray(new String[0]));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBorder(BorderFactory.createTitledBorder("Contacts"));

        groupsModel = new DefaultListModel<>();
        groupList = new JList<>(groupsModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBorder(BorderFactory.createTitledBorder("Groups"));

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
        
        chatClient = new SSLChatClient(
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
        JButton btnSendGroup = new JButton("Send to Group");
        JButton btnAddUserToGroup = new JButton("Add User to Group");
        btnAddUserToGroup.addActionListener(e -> addUserToGroup());
        topButtons.add(btnAddUserToGroup);
        
        btnCreateGroup.addActionListener(e -> createGroup());
        btnSendGroup.addActionListener(e -> sendGroupMessage());

        topButtons.add(btnCreateGroup);
        topButtons.add(btnSendGroup);

        add(topButtons, BorderLayout.NORTH);
        
        JButton btnSend = new JButton("Send");
        btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSend.setFocusPainted(false);
        btnSend.setBackground(new Color(52, 73, 94));
        btnSend.setForeground(Color.BLACK);
        Dimension fieldSize = inputField.getPreferredSize();
        btnSend.setPreferredSize(new Dimension(80, fieldSize.height));
        btnSend.addActionListener(e -> sendMessage());
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);
        
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        add(bottomPanel, BorderLayout.SOUTH);
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

        if("Service".equals(selectedUser)) {
        	log.warning("Error - You cannot add Service to a group.");
            JOptionPane.showMessageDialog(this, "You cannot add Service to a group.");
            return;
        }
        
        chatClient.sendMessage("ADD_TO_GROUP " + selectedGroup + " " + selectedUser);
        log.info(currentUser + " added " + selectedUser + " to a group " + selectedGroup + ".");
        chatArea.append("You added " + selectedUser + " to group " + selectedGroup + ".\n");
    }

    private void sendMessage() {
        String to = userList.getSelectedValue();
        String msg = inputField.getText().trim();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String sendTime = LocalDateTime.now().format(formatter);
        
		msg = msg + " time:" + sendTime;
        if (to == null || msg.isEmpty()) {
        	log.warning("Error - select user to send message.");
            JOptionPane.showMessageDialog(this, "Select user to send message.");
        	return;
        }

        if("Service".equals(to)) {
        	chatClient.sendMessage("#service: " + msg);
	        chatArea.append("[You->Service]:\n" + msg + "\n");
	        inputField.setText("");
	        log.info(currentUser + " sent to service: " + msg);
	        return;
        }     
        
        chatClient.sendMessage("@" + to + ": " + msg);
        log.info("Message sent to: " + to);
        chatArea.append("[You->" + to + "]:\n" + msg + "\n");
        inputField.setText("");
    }

    private void sendGroupMessage() {
        String group = groupList.getSelectedValue();
        String msg = inputField.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String sendTime = LocalDateTime.now().format(formatter);
        
		msg = msg + " time:" + sendTime;
        if (group == null || msg.isEmpty()) return;
        chatClient.sendMessageMulticast(group, msg);
        log.info("Message sent to group: " + group);
        chatArea.append("[You->" + group + "]:\n" + msg + "\n");
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

}

