package net.etfbl.mdp.service.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.etfbl.mdp.service.securechat.SSLChatServer;

public class ServiceMain extends JFrame {

	private CardLayout cardLayout;
    private JPanel mainPanel;
	
	public ServiceMain() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Service Administration Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        ClientPanel clientPanel = new ClientPanel();
        ChatServerPanel chatPanel = new ChatServerPanel();
        AppointmentPanel appointmentPanel = new AppointmentPanel();
        PartsPanel partsPanel = new PartsPanel();
        SupplierOrdersPanel supplierOrdersPanel = new SupplierOrdersPanel();
        InvoicePanel invoicePanel = new InvoicePanel();
        new Thread(new SSLChatServer()).start();
        mainPanel.add(clientPanel, "clients");
        mainPanel.add(chatPanel, "chat");
        chatPanel.registerListener();
        mainPanel.add(appointmentPanel, "appointments");
        mainPanel.add(partsPanel, "parts");
        mainPanel.add(supplierOrdersPanel, "supplier");
        mainPanel.add(invoicePanel, "invoice");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(245, 247, 250));
        JButton btnClients = new JButton("Clients");
        JButton btnChat = new JButton("Chat Server");
        JButton btnExit = new JButton("Exit");
        JButton btnAppointments = new JButton("Termins");
        JButton btnParts = new JButton("Parts");
        JButton btnSupplier = new JButton("Orders");
        JButton btnInvoice = new JButton("Invoices");

        for (JButton b : new JButton[]{btnClients, btnChat, btnExit, btnAppointments, btnParts, btnSupplier, btnInvoice}) {
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(52, 73, 94));
            b.setForeground(Color.BLACK);
        }


        topPanel.add(btnClients);
        topPanel.add(btnAppointments);
        topPanel.add(btnParts);
        topPanel.add(btnSupplier);
        topPanel.add(btnInvoice);
        topPanel.add(btnChat);
        topPanel.add(btnExit);
        
        
        
        btnClients.addActionListener(e -> cardLayout.show(mainPanel, "clients"));
        btnChat.addActionListener(e -> cardLayout.show(mainPanel, "chat"));
        btnExit.addActionListener(e -> dispose());
        btnAppointments.addActionListener(e -> cardLayout.show(mainPanel, "appointments"));
        btnParts.addActionListener(e -> cardLayout.show(mainPanel, "parts"));
        btnSupplier.addActionListener(e -> cardLayout.show(mainPanel, "supplier"));
        btnInvoice.addActionListener(e -> cardLayout.show(mainPanel, "invoice"));

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ServiceMain().setVisible(true));
	}
	
}
