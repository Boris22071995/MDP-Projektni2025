package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.gui.rest.RestAppointment;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

	private JTable table;
    private DefaultTableModel model;
    private RestAppointment restAppointment = new RestAppointment();
    private ServiceMain serviceMain;
    public AppointmentPanel(ServiceMain serviceMain) {
    	this.serviceMain = serviceMain;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Appointment scheduling"));

        model = new DefaultTableModel(new Object[]{"User", "Date", "Time", "Type", "Status", "Description", "Comment"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnComment = new JButton("Comment");
        JButton btnCreateInvoice = new JButton("Create invoice");
        for (JButton b : new JButton[]{btnRefresh, btnApprove, btnReject, btnComment,btnCreateInvoice}) {
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(52, 73, 94));
            b.setForeground(Color.BLACK);
        }
        

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnApprove);
        bottomPanel.add(btnReject);
        bottomPanel.add(btnComment);
        bottomPanel.add(btnCreateInvoice);
        add(bottomPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadAppointments());
        btnApprove.addActionListener(e -> approveAppointment());
        btnReject.addActionListener(e -> rejectAppointment());
        btnComment.addActionListener(e -> addComment());
        btnCreateInvoice.addActionListener(e -> createInvoice());
        loadAppointments();
    }
    
    private void createInvoice() {
    	int row = table.getSelectedRow();
    	if(row < 0) {
    		JOptionPane.showMessageDialog(this, "Select appointment first!");
    		return;
    	}
    	
    	String username = model.getValueAt(row, 0).toString();
    	String date = model.getValueAt(row, 1).toString();
    	String time = model.getValueAt(row, 2).toString();
    	
    	Appointment selected = null;
    	for(Appointment a : restAppointment.getAllAppointments()) {
    		if(a.getOwnerUsername().equals(username) && a.getDate().equals(date)&&a.getTime().equals(time)) {
    			selected = a;
    			break;
    		}
    	}
    	
    	if(selected == null) return;
    	serviceMain.showInvoicePanel(selected);
    }
    
//    private void openInvoicePanel() {
//    	serviceMain.showInvoicePanel();
//	}

	private void loadAppointments() {
        model.setRowCount(0);
        List<Appointment> list = restAppointment.getAllAppointments(); // REST poziv

        if (list != null) {
            for (Appointment a : list) {
                model.addRow(new Object[]{
                        a.getOwnerUsername(), a.getDate(), a.getTime(), a.getType(), a.getStatus(), a.getDescription(), a.getComment()
                });
            }
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
    
    private void approveAppointment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = model.getValueAt(row, 0).toString();
            String date = model.getValueAt(row, 1).toString();
            String time = model.getValueAt(row, 2).toString();
            List<Appointment> appointments = restAppointment.getAllAppointments();
            Appointment a = new Appointment();
            for(int i = 0; i < appointments.size(); i++) {
            	if(username.equalsIgnoreCase(appointments.get(i).getOwnerUsername()) && date.equalsIgnoreCase(appointments.get(i).getDate()) && time.equalsIgnoreCase(appointments.get(i).getTime())) {
            		a = appointments.get(i);
            	}
            }
            restAppointment.approveAppointment(a.getId()); 
            loadAppointments();
        }
    }

    private void rejectAppointment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
        	String username = model.getValueAt(row, 0).toString();
            String date = model.getValueAt(row, 1).toString();
            String time = model.getValueAt(row, 2).toString();
            List<Appointment> appointments = restAppointment.getAllAppointments();
            Appointment a = new Appointment();
            for(int i = 0; i < appointments.size(); i++) {
            	if(username.equalsIgnoreCase(appointments.get(i).getOwnerUsername()) && date.equalsIgnoreCase(appointments.get(i).getDate()) && time.equalsIgnoreCase(appointments.get(i).getTime())) {
            		a = appointments.get(i);
            	}
            }
            restAppointment.rejectAppointment(a.getId());
            loadAppointments();
        }
    }

    private void addComment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
        	String username = model.getValueAt(row, 0).toString();
            String date = model.getValueAt(row, 1).toString();
            String time = model.getValueAt(row, 2).toString();
            List<Appointment> appointments = restAppointment.getAllAppointments();
            Appointment a = new Appointment();
            for(int i = 0; i < appointments.size(); i++) {
            	if(username.equalsIgnoreCase(appointments.get(i).getOwnerUsername()) && date.equalsIgnoreCase(appointments.get(i).getDate()) && time.equalsIgnoreCase(appointments.get(i).getTime())) {
            		a = appointments.get(i);
            	}
            }
            String comment = JOptionPane.showInputDialog(this, "Insert comment for client " + username + ":");
            if (comment != null) {
            	restAppointment.addComment(a.getId(), comment); // TODO
                JOptionPane.showMessageDialog(this, "Comment added!");
                loadAppointments();
            }
        }
    }
	
}
