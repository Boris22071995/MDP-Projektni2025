package net.etfbl.mdp.service.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.etfbl.mdp.model.Client;

public class ClientTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private String[] columns = { "Username", "Name", "Surname", "Vehicle data", "Approved", "Blocked" };
	private List<Client> clients = new ArrayList<>();

	@Override
	public int getRowCount() {
		return clients.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Client c = clients.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return c.getUsername();
		case 1:
			return c.getName();
		case 2:
			return c.getLastName();
		case 3:
			return c.getVehicleData();
		case 4:
			return c.isApproved() ? "Approved" : "Not approved";
		case 5:
			return c.isBlocked() ? "Blocked" : "Not blocked";
		default:
			return null;
		}
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
		fireTableDataChanged();

	}

	public Client getClientAt(int row) {
		return clients.get(row);
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

}
