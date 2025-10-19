package net.etfbl.mdp.service.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.ClientService;

public class ClientPanel extends JPanel {

	private JTable table;
	private ClientTableModel tableModel;
	private ClientService service;
	
	public ClientPanel() {
		service = new ClientService();
		
		setLayout(new BorderLayout());
		
		tableModel = new ClientTableModel();
		table = new JTable(tableModel);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton refreshBtn = new JButton("Refresh");
		JButton approveBtn = new JButton("Approve");
		JButton blockBtn = new JButton("Block");
		JButton deleteBtn = new JButton("Delete");
		
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
		List<Client> clients = service.getAllClients();
		tableModel.setClients(clients);
	}
	
	private void approveClient(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = tableModel.getClientAt(row).getUsername();
            service.approveClient(username);
            loadClients();
        } else {
            JOptionPane.showMessageDialog(this, "Approve client!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
	
	private void blockClient(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = tableModel.getClientAt(row).getUsername();
            service.blockClient(username);
            loadClients();
        } else {
            JOptionPane.showMessageDialog(this, "Odaberite klijenta!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
        }
    }
	
	private void deleteClient(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = tableModel.getClientAt(row).getUsername();
            int confirm = JOptionPane.showConfirmDialog(this, "Da li ste sigurni da želite obrisati klijenta " + username + "?", "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.delete(username);
                loadClients();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Odaberite klijenta!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
        }
    }

}
