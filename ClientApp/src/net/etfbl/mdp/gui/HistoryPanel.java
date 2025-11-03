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
          setLayout(new BorderLayout(10, 10));
          setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
          setBackground(Color.WHITE);

          JLabel title = new JLabel("Service history", SwingConstants.CENTER);
          title.setFont(new Font("Segoe UI", Font.BOLD, 18));
          add(title, BorderLayout.NORTH);

          model = new DefaultTableModel(new Object[]{"Date", "Time", "Type", "Status", "Description"}, 0);
          table = new JTable(model);
          table.setRowHeight(26);
          table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
          add(new JScrollPane(table), BorderLayout.CENTER);

          JButton btnLoad = new JButton("Refresh");
          btnLoad.setBackground(new Color(52, 73, 94));
          btnLoad.setForeground(Color.BLACK);
          btnLoad.addActionListener(e -> loadHistory());
          add(btnLoad, BorderLayout.SOUTH);

          loadHistory();
    	
    }

    private void loadHistory() {
    	
    	 model.setRowCount(0);
         List<Appointment> appointments = service.getAppointmentsByUser(client.getUsername());
         for (Appointment a : appointments) {
             model.addRow(new Object[]{
                     a.getDate(), a.getTime(), a.getType(), a.getStatus(), a.getDescription()
             });
         }
    	
    }
}
