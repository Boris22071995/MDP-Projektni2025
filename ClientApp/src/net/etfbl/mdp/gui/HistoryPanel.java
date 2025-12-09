package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AppointmentService;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class HistoryPanel extends JPanel {
	private static final Logger log = AppLogger.getLogger();
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

          model = new DefaultTableModel(new Object[]{"Date", "Time", "Type", "Status", "Description", "Comment"}, 0);
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
                     a.getDate(), a.getTime(), a.getType(), a.getStatus(), a.getDescription(), a.getComment()
             });
         }
         
         table.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
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
    	log.info("Reservation history loaded.");
    }
}
