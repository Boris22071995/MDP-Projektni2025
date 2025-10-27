package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Appointment.Status;
import net.etfbl.mdp.service.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryPanel extends JPanel {

	private JTable table;
    private DefaultTableModel model;

    public HistoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Service history"));

        model = new DefaultTableModel(new Object[]{"Date", "Time", "Type", "Status", "Description"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnLoad = new JButton("Refresh");
        btnLoad.addActionListener(e -> loadHistory());

        add(btnLoad, BorderLayout.SOUTH);

        loadHistory();
    }

    private void loadHistory() {
        model.setRowCount(0);
        // Test podaci - kasnije ide REST poziv
        model.addRow(new Object[]{"2025-10-20", "09:30", "Redovni servis", "Završeno", "Zamjena ulja"});
        model.addRow(new Object[]{"2025-10-22", "14:00", "Popravka", "U toku", "Zamjena kočnica"});
    }
}
