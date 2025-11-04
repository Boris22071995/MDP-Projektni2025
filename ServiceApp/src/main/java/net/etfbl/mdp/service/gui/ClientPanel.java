package net.etfbl.mdp.service.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.ClientService;
import net.etfbl.mdp.service.gui.rest.RestClient;

public class ClientPanel extends JPanel {

	private JTable table;
	private ClientTableModel tableModel;
	private RestClient restClient;

	public ClientPanel() {
		restClient = new RestClient();
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel title = new JLabel("Client list", SwingConstants.CENTER);
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);

		tableModel = new ClientTableModel();
		table = new JTable(tableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(24);
		table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		buttonsPanel.setBackground(Color.WHITE);

		JButton refreshBtn = new JButton("Refresh");
		JButton approveBtn = new JButton("Approve");
		JButton blockBtn = new JButton("Block");
		JButton deleteBtn = new JButton("Delete");

		for (JButton btn : new JButton[] { refreshBtn, approveBtn, blockBtn, deleteBtn }) {
			btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
			btn.setFocusPainted(false);
			btn.setBackground(new Color(52, 73, 94));
			btn.setForeground(Color.BLACK);
		}

		buttonsPanel.add(refreshBtn);
		buttonsPanel.add(approveBtn);
		buttonsPanel.add(blockBtn);
		buttonsPanel.add(deleteBtn);

		add(buttonsPanel, BorderLayout.SOUTH);

		refreshBtn.addActionListener(e -> loadClients());
		approveBtn.addActionListener(this::approveClient);
		blockBtn.addActionListener(this::blockClient);
		deleteBtn.addActionListener(this::deleteClient);

		loadClients();

	}

	private void loadClients() {
		List<Client> clients = restClient.getAllClients();
		tableModel.setClients(clients);
	}

	private void approveClient(ActionEvent e) {

		int row = table.getSelectedRow();
		if (row >= 0) {
			String username = tableModel.getClientAt(row).getUsername();
			restClient.approveClient(username);
			loadClients();
		} else {
			JOptionPane.showMessageDialog(this, "Choose client!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void blockClient(ActionEvent e) {

		int row = table.getSelectedRow();
		if (row >= 0) {
			Client c = tableModel.getClientAt(row);
			String username = c.getUsername();

			if (c.isBlocked()) {
				int confirm = JOptionPane.showConfirmDialog(this, "Do you want to UNBLOCK client " + username + "?",
						"Approve", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					restClient.unblockClient(username);
					JOptionPane.showMessageDialog(this, "Client " + username + " is unblocked.");
				}
			} else {
				int confirm = JOptionPane.showConfirmDialog(this, "Do you want to BLOCK client " + username + "?",
						"Approve", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					restClient.blockClient(username);
					JOptionPane.showMessageDialog(this, "Client " + username + " is blocked.");
				}
			}

			loadClients();
		} else {
			JOptionPane.showMessageDialog(this, "Choose client!", "Warning", JOptionPane.WARNING_MESSAGE);
		}

	}

	private void deleteClient(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row >= 0) {
			String username = tableModel.getClientAt(row).getUsername();
			int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete client: " + username + "?",
					"Approve", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				restClient.deleteClient(username);
				loadClients();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Choose client!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

}
