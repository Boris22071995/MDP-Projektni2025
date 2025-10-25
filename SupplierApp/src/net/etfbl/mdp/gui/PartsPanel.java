package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.server.SupplierServer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PartsPanel extends JPanel {
	
	private JTable table;
	private DefaultTableModel model;
	
	public PartsPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"Article number", "Title", "Price (€)", "Image url"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Osvježi");
        refreshBtn.addActionListener(e -> loadParts());
        add(refreshBtn, BorderLayout.SOUTH);

        loadParts();
    }
	
	private void loadParts() {
        model.setRowCount(0);
        List<Part> list = SupplierServer.getParts();
        for (Part p : list) {
            model.addRow(new Object[]{p.getCode(), p.getTitle(), p.getPrice(), p.getImageURL()});
        }
    }

}
