package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SupplierOrdersPanel extends JPanel {

	  private JTable table;
	    private DefaultTableModel model;

	    public SupplierOrdersPanel() {
	        setLayout(new BorderLayout());
	        setBorder(BorderFactory.createTitledBorder("Orders to suppliers"));

	        model = new DefaultTableModel(new Object[]{"Id", "Quantity", "Supplier", "Status"}, 0);
	        table = new JTable(model);
	        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        add(new JScrollPane(table), BorderLayout.CENTER);

	        JPanel panel = new JPanel(new FlowLayout());
	        JButton btnOrder = new JButton("Send request");
	        JButton btnRefresh = new JButton("Refresh");
	        
	        for (JButton b : new JButton[]{btnOrder, btnRefresh}) {
	            b.setFocusPainted(false);
	            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
	            b.setBackground(new Color(52, 73, 94));
	            b.setForeground(Color.BLACK);
	        }
	        
	        panel.add(btnOrder);
	        panel.add(btnRefresh);
	        add(panel, BorderLayout.SOUTH);

	        // TODO: RabbitMQ logika i komunikacija sa SupplierApp
	
}
}
