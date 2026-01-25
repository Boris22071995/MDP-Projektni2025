package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.service.gui.rest.RestParts;
import net.etfbl.mdp.util.AppLogger;

public class PartsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();

	private JTable table;
	private DefaultTableModel model;
	private RedisPartService service;
	private RestParts restParts;

	public PartsPanel(RedisPartService service) {
		restParts = new RestParts();
		this.service = service;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Parts"));

		model = new DefaultTableModel(new Object[] { "Id", "Name", "Manufacturer", "Price", "Quantity", "Description" },
				0);
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public String getToolTipText(MouseEvent e) {
				Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);

				if (row >= 0 && col >= 0) {
					Object value = getValueAt(row, col);
					if (value != null) {
						return value.toString();
					}
				}
				return null;
			}
		};
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel panel = new JPanel(new FlowLayout());
		JButton btnAdd = new JButton("Add");
		JButton btnEdit = new JButton("Change");
		JButton btnDelete = new JButton("Delete");
		JButton btnRefresh = new JButton("Refresh");

		for (JButton b : new JButton[] { btnAdd, btnEdit, btnDelete, btnRefresh }) {
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

		btnAdd.addActionListener(e -> addPart());
		btnEdit.addActionListener(e -> editPart());
		btnDelete.addActionListener(e -> deletePart());
		btnRefresh.addActionListener(e -> loadParts());

		loadParts();
	}

	private void loadParts() {
		model.setRowCount(0);
		List<Part> parts = restParts.getAllParts();

		if (parts != null) {
			for (Part p : parts) {
				model.addRow(new Object[] { p.getId(), p.getName(), p.getManufacturer(), p.getPrice(), p.getQuantity(),
						p.getDescription() });
			}
		}
	}

	private void addPart() {
		JTextField name = new JTextField();
		JTextField manu = new JTextField();
		JTextField price = new JTextField();
		JTextField qty = new JTextField();
		JTextArea desc = new JTextArea(3, 20);

		Object[] fields = { "Name:", name, "Manufacturer:", manu, "Price:", price, "Quantity:", qty, "Description:",
				new JScrollPane(desc) };

		int result = JOptionPane.showConfirmDialog(this, fields, "Add new part", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Part p = new Part(UUID.randomUUID().toString().substring(0, 8), name.getText(), manu.getText(),
					Double.parseDouble(price.getText()), Integer.parseInt(qty.getText()), desc.getText());
			restParts.addPart(p);
			loadParts();
		}
	}

	private void editPart() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a part to edit.");
			return;
		}

		String id = (String) model.getValueAt(row, 0);
		JTextField name = new JTextField((String) model.getValueAt(row, 1));
		JTextField manu = new JTextField((String) model.getValueAt(row, 2));
		JTextField price = new JTextField(model.getValueAt(row, 3).toString());
		JTextField qty = new JTextField(model.getValueAt(row, 4).toString());
		JTextArea desc = new JTextArea((String) model.getValueAt(row, 5));

		Object[] fields = { "Name:", name, "Manufacturer:", manu, "Price:", price, "Quantity:", qty, "Description:",
				new JScrollPane(desc) };

		int result = JOptionPane.showConfirmDialog(this, fields, "Edit part", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Part p = new Part(id, name.getText(), manu.getText(), Double.parseDouble(price.getText()),
					Integer.parseInt(qty.getText()), desc.getText());
			restParts.updatePart(p);
			log.info("Part is changed.");
			loadParts();
		}
	}

	private void deletePart() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a part to delete.");
			log.warning("Selection of part is needed.");
			return;
		}
		String id = (String) model.getValueAt(row, 0);
		int conf = JOptionPane.showConfirmDialog(this, "Delete part " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (conf == JOptionPane.YES_OPTION) {
			log.info("Part is deleted.");
			restParts.deletePart(id);
			loadParts();
		}
	}

}
