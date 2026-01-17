package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.gui.rest.RestAppointment;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();

	private JTable table;
	private DefaultTableModel model;
	private RestAppointment restAppointment = new RestAppointment();
	private ServiceMain serviceMain;

	public AppointmentPanel(ServiceMain serviceMain) {
		this.serviceMain = serviceMain;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Appointment scheduling"));

		model = new DefaultTableModel(
				new Object[] { "User", "Date", "Time", "Type", "Status", "Description", "Comment" }, 0);
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public String getToolTipText(MouseEvent e) {
				Point p =e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				
				if(row >=0 && col >=0) {
					Object value = getValueAt(row, col);
					if(value != null) {
						return value.toString();
					}
				}
				return null;
			}
		};
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton btnRefresh = new JButton("Refresh");
		JButton btnApprove = new JButton("Approve");
		JButton btnReject = new JButton("Reject");
		JButton btnComment = new JButton("Comment");
		JButton btnCreateInvoice = new JButton("Create invoice");
		JButton btnDeleteAppointment = new JButton("Delete");
		for (JButton b : new JButton[] { btnRefresh, btnApprove, btnReject, btnComment, btnCreateInvoice, btnDeleteAppointment }) {
			b.setFocusPainted(false);
			b.setFont(new Font("Segoe UI", Font.BOLD, 14));
			b.setBackground(new Color(52, 73, 94));
			b.setForeground(Color.BLACK);
		}

		bottomPanel.add(btnRefresh);
		bottomPanel.add(btnApprove);
		bottomPanel.add(btnReject);
		bottomPanel.add(btnComment);
		bottomPanel.add(btnDeleteAppointment);
		bottomPanel.add(btnCreateInvoice);
		add(bottomPanel, BorderLayout.SOUTH);
		btnRefresh.addActionListener(e -> loadAppointments());
		btnApprove.addActionListener(e -> approveAppointment());
		btnReject.addActionListener(e -> rejectAppointment());
		btnComment.addActionListener(e -> addComment());
		btnCreateInvoice.addActionListener(e -> createInvoice());
		btnDeleteAppointment.addActionListener(e -> deleteAppointment());
		loadAppointments();
	}

	private void createInvoice() {
		int row = table.getSelectedRow();
		if (row < 0) {
			log.warning("Appointment selection is needed.");
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			return;
		}

		String username = model.getValueAt(row, 0).toString();
		String date = model.getValueAt(row, 1).toString();
		String time = model.getValueAt(row, 2).toString();

		Appointment selected = null;
		for (Appointment a : restAppointment.getAllAppointments()) {
			if (a.getOwnerUsername().equals(username) && a.getDate().equals(date) && a.getTime().equals(time)) {
				selected = a;
				break;
			}
		}

		if (selected == null) return;
		serviceMain.showInvoicePanel(selected);
	}

	private void loadAppointments() {
		model.setRowCount(0);
		List<Appointment> list = restAppointment.getAllAppointments();

		if (list != null) {
			for (Appointment a : list) {
				model.addRow(new Object[] { a.getOwnerUsername(), a.getDate(), a.getTime(), a.getType(), a.getStatus(),
						a.getDescription(), a.getComment() });
			}
			log.info("Appointments are loaded");
		}

		table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
			JLabel label = new JLabel(value != null ? value.toString() : "");
			if (value != null) {
				String status = value.toString().toLowerCase();
				if (status.contains("approved")) {
					label.setForeground(new Color(39, 174, 96));
					label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				} else if (status.contains("rejected")) {
					label.setForeground(new Color(192, 57, 43));
					label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				} else {
					label.setForeground(new Color(243, 156, 18));
					label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				}
			} else {
				label.setBackground(Color.WHITE);
			}

			return label;
		});
	}

	private void deleteAppointment() {
		int row = table.getSelectedRow();
		if (row < 0) {
			log.warning("Appointment selection is needed for delete.");
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			return;
		}
		
		if (row >= 0) {
			String username = model.getValueAt(row, 0).toString();
			String date = model.getValueAt(row, 1).toString();
			String time = model.getValueAt(row, 2).toString();
			List<Appointment> appointments = restAppointment.getAllAppointments();
			Appointment a = new Appointment();
			for (int i = 0; i < appointments.size(); i++) {
				if (username.equalsIgnoreCase(appointments.get(i).getOwnerUsername())
						&& date.equalsIgnoreCase(appointments.get(i).getDate())
						&& time.equalsIgnoreCase(appointments.get(i).getTime())) {
					a = appointments.get(i);
				}
			}	
		restAppointment.deleteAppointmetn(a);
		log.info("Appointment is deleted.");
		JOptionPane.showMessageDialog(this, "Appointment is deleted!");
		loadAppointments();
	}
}
	
	private void approveAppointment() {
		int row = table.getSelectedRow();
		if (row < 0) {
			log.warning("Appointment selection is needed for approve.");
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			return;
		}
		if (row >= 0) {
			String username = model.getValueAt(row, 0).toString();
			String date = model.getValueAt(row, 1).toString();
			String time = model.getValueAt(row, 2).toString();
			List<Appointment> appointments = restAppointment.getAllAppointments();
			Appointment a = new Appointment();
			for (int i = 0; i < appointments.size(); i++) {
				if (username.equalsIgnoreCase(appointments.get(i).getOwnerUsername())
						&& date.equalsIgnoreCase(appointments.get(i).getDate())
						&& time.equalsIgnoreCase(appointments.get(i).getTime())) {
					a = appointments.get(i);
				}
			}
			restAppointment.approveAppointment(a.getId());
			log.info("Appointment is approved.");
			JOptionPane.showMessageDialog(this, "Appointment is added!");
			loadAppointments();
		}
	}

	private void rejectAppointment() {
		int row = table.getSelectedRow();
		if (row < 0) {
			log.warning("Appointment selection is needed for rejecting.");
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			return;
		}
		if (row >= 0) {
			String username = model.getValueAt(row, 0).toString();
			String date = model.getValueAt(row, 1).toString();
			String time = model.getValueAt(row, 2).toString();
			List<Appointment> appointments = restAppointment.getAllAppointments();
			Appointment a = new Appointment();
			for (int i = 0; i < appointments.size(); i++) {
				if (username.equalsIgnoreCase(appointments.get(i).getOwnerUsername())
						&& date.equalsIgnoreCase(appointments.get(i).getDate())
						&& time.equalsIgnoreCase(appointments.get(i).getTime())) {
					a = appointments.get(i);
				}
			}
			restAppointment.rejectAppointment(a.getId());
			log.info("Appointment is rejected.");
			JOptionPane.showMessageDialog(this, "Appointment is rejected!");
			loadAppointments();
		}
	}

	private void addComment() {
		int row = table.getSelectedRow();
		if (row < 0) {
			log.warning("Appointment selection is needed for adding comment.");
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			return;
		}
		if (row >= 0) {
			String username = model.getValueAt(row, 0).toString();
			String date = model.getValueAt(row, 1).toString();
			String time = model.getValueAt(row, 2).toString();
			List<Appointment> appointments = restAppointment.getAllAppointments();
			Appointment a = new Appointment();
			for (int i = 0; i < appointments.size(); i++) {
				if (username.equalsIgnoreCase(appointments.get(i).getOwnerUsername())
						&& date.equalsIgnoreCase(appointments.get(i).getDate())
						&& time.equalsIgnoreCase(appointments.get(i).getTime())) {
					a = appointments.get(i);
				}
			}
			String comment = JOptionPane.showInputDialog(this, "Insert comment for client " + username + ":");
			if (comment != null) {
				restAppointment.addComment(a.getId(), comment);
				log.info("Commented is added.");
				JOptionPane.showMessageDialog(this, "Comment added!");
				loadAppointments();
			}
		}
	}

}
