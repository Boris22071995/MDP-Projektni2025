package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    AppointmentService service = new AppointmentService();
	private JTable table;
    private DefaultTableModel model;
    Client client = new Client();

    public HistoryPanel(Client client) {
    	this.client = client;
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
    	List<Appointment> appointments = service.getAppointmentsByUser(client.getUsername());
    	for(int i = 0; i < appointments.size(); i++) {
    		 model.setRowCount(i);
    	     model.addRow(new Object[]{appointments.get(i).getDate(), appointments.get(i).getTime(), appointments.get(i).getType(),
    	    		 appointments.get(i).getStatus(), appointments.get(i).getDescription()});
    	}
    }
}
