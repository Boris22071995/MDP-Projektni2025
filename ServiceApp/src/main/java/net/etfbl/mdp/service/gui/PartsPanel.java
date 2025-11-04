package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PartsPanel extends JPanel {

	 private JTable table;
	    private DefaultTableModel model;

	    public PartsPanel() {
	        setLayout(new BorderLayout());
	        setBorder(BorderFactory.createTitledBorder("Parts"));

	        model = new DefaultTableModel(new Object[]{"Id", "Name", "Manufacturer", "Price", "Quantity", "Description"}, 0);
	        table = new JTable(model);
	        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        add(new JScrollPane(table), BorderLayout.CENTER);

	        JPanel panel = new JPanel(new FlowLayout());
	        JButton btnAdd = new JButton("Add");
	        JButton btnEdit = new JButton("Change");
	        JButton btnDelete = new JButton("Delete");
	        JButton btnRefresh = new JButton("Refresh");
	        
	        for (JButton b : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh}) {
	            b.setFocusPainted(false);
	            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
	            b.setBackground(new Color(52, 73, 94));
	            b.setForeground(Color.BLACK);
	        }
	        
	        panel.add(btnAdd);
	        panel.add(btnEdit);
	        panel.add(btnDelete);
	        panel.add(btnRefresh);
	        add(panel, BorderLayout.SOUTH);

	        // TODO: CRUD logika kada povežeš sa Redis servisom
	    }
	
}
