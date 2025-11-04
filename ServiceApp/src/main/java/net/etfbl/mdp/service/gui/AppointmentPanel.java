package net.etfbl.mdp.service.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.gui.rest.RestAppointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

	private JTable table;
    private DefaultTableModel model;
    private RestAppointment restAppointment = new RestAppointment();
    
    public AppointmentPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Appointment scheduling"));

        model = new DefaultTableModel(new Object[]{"User", "Date", "Time", "Type", "Status", "Description"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnComment = new JButton("Comment");
        for (JButton b : new JButton[]{btnRefresh, btnApprove, btnReject, btnComment}) {
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(52, 73, 94));
            b.setForeground(Color.BLACK);
        }
        

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnApprove);
        bottomPanel.add(btnReject);
        bottomPanel.add(btnComment);
        add(bottomPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadAppointments());
        btnApprove.addActionListener(e -> approveAppointment());
        btnReject.addActionListener(e -> rejectAppointment());
        btnComment.addActionListener(e -> addComment());

        loadAppointments();
    }

    private void loadAppointments() {
        model.setRowCount(0);
        List<Appointment> list = restAppointment.getAllAppointments(); // TODO: Implement REST metoda
        if (list != null) {
            for (Appointment a : list) {
                model.addRow(new Object[]{
                        a.getOwnerUsername(), a.getDate(), a.getTime(), a.getType(), a.getStatus(), a.getDescription()
                });
            }
        }
    }
    
    private void approveAppointment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = model.getValueAt(row, 0).toString();
           // restAppointment.approveAppointment(username); // TODO
            System.out.println("NAGODITI STATUSE I IZMJENE");
            loadAppointments();
        }
    }

    private void rejectAppointment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = model.getValueAt(row, 0).toString();
           // restAppointment.rejectAppointment(username); // TODO
            System.out.println("NAGODITI STATUSE I IZMJENE");
            loadAppointments();
        }
    }

    private void addComment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = model.getValueAt(row, 0).toString();
            String comment = JOptionPane.showInputDialog(this, "Insert comment for client " + username + ":");
            if (comment != null) {
            	//restAppointment.addComment(username, comment); // TODO
            	System.out.println("NAGODITI KOMENTARE");
                JOptionPane.showMessageDialog(this, "Comment added!");
                loadAppointments();
            }
        }
    }
	
}
