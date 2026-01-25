package net.etfbl.mdp.service.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.service.securechat.SSLChatServer;
import net.etfbl.mdp.util.ConfigurationLoader;

public class ServiceMain extends JFrame {

	private static final long serialVersionUID = 1L;

	private CardLayout cardLayout;
	private JPanel mainPanel;
	private InvoicePanel invoicePanel;
	private RedisPartService redisService = new RedisPartService();

	public ServiceMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		setTitle("Service Administration Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		ClientPanel clientPanel = new ClientPanel();
		ChatServerPanel chatPanel = new ChatServerPanel();
		AppointmentPanel appointmentPanel = new AppointmentPanel(this);
		PartsPanel partsPanel = new PartsPanel(redisService);
		SupplierOrdersPanel supplierOrdersPanel = new SupplierOrdersPanel(redisService);
		new Thread(new SSLChatServer()).start();
		mainPanel.add(clientPanel, "clients");
		mainPanel.add(chatPanel, "chat");
		chatPanel.registerListener();
		mainPanel.add(appointmentPanel, "appointments");
		mainPanel.add(partsPanel, "parts");
		mainPanel.add(supplierOrdersPanel, "supplier");
		invoicePanel = new InvoicePanel();
		mainPanel.add(invoicePanel, "invoice");

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		topPanel.setBackground(new Color(245, 247, 250));
		JButton btnClients = new JButton("Clients");
		JButton btnChat = new JButton("Chat Server");
		JButton btnExit = new JButton("Exit");
		JButton btnAppointments = new JButton("Termins");
		JButton btnParts = new JButton("Parts");
		JButton btnSupplier = new JButton("Orders");

		for (JButton b : new JButton[] { btnClients, btnChat, btnExit, btnAppointments, btnParts, btnSupplier }) {
			b.setFocusPainted(false);
			b.setFont(new Font("Segoe UI", Font.BOLD, 14));
			b.setBackground(new Color(52, 73, 94));
			b.setForeground(Color.BLACK);
		}

		topPanel.add(btnClients);
		topPanel.add(btnAppointments);
		topPanel.add(btnParts);
		topPanel.add(btnSupplier);
		topPanel.add(btnChat);
		topPanel.add(btnExit);

		btnClients.addActionListener(e -> cardLayout.show(mainPanel, "clients"));
		btnChat.addActionListener(e -> cardLayout.show(mainPanel, "chat"));
		btnExit.addActionListener(e -> dispose());
		btnAppointments.addActionListener(e -> cardLayout.show(mainPanel, "appointments"));
		btnParts.addActionListener(e -> cardLayout.show(mainPanel, "parts"));
		btnSupplier.addActionListener(e -> cardLayout.show(mainPanel, "supplier"));

		add(topPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}

	public void showInvoicePanel(Appointment appointment) {
		invoicePanel.loadAppointment(appointment);
		cardLayout.show(mainPanel, "invoice");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ServiceMain().setVisible(true));
	     String FILE_PATH = ConfigurationLoader.getString("clients.filepath");
		System.out.println(ConfigurationLoader.getString("clients.filepath"));
		
		
	}

}
