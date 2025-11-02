package net.etfbl.mdp.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.etfbl.mdp.messaging.OrderConsumer;

public class SupplierMain extends JFrame{

	private CardLayout cardLayout;
    private JPanel cardPanel;

    public SupplierMain() {
    	 new Thread(new OrderConsumer()).start();
        setTitle("Supplier - Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Glavni layout
        setLayout(new BorderLayout());

        // Gornja navigacija
        JPanel navPanel = new JPanel();
        JButton btnParts = new JButton("Manage Parts");
        JButton btnOrders = new JButton("Order Queue");
        navPanel.add(btnParts);
        navPanel.add(btnOrders);
        add(navPanel, BorderLayout.NORTH);

        // Centralni sadržaj
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Dodaj panele
        cardPanel.add(new PartsPanel(), "parts");
        cardPanel.add(new OrderQueuePanel(), "orders");

        add(cardPanel, BorderLayout.CENTER);

        // Dugmad — prebacivanje između panela
        btnParts.addActionListener(e -> cardLayout.show(cardPanel, "parts"));
        btnOrders.addActionListener(e -> cardLayout.show(cardPanel, "orders"));
        
        JButton btnInvoices = new JButton("Invoices");
        navPanel.add(btnInvoices);
        cardPanel.add(new InvoicesPanel(), "invoices");
        btnInvoices.addActionListener(e -> cardLayout.show(cardPanel, "invoices"));
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierMain().setVisible(true));
        
    }
	
}
