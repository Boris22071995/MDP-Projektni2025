package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AppointmentService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationPanel extends JPanel {

	 private JTextField dateField;
	    private JTextField timeField;
	    private JComboBox<String> serviceTypeBox;
	    private JTextArea descriptionArea;
	    AppointmentService service = new AppointmentService();
	    Client client = new Client();

	    public ReservationPanel(Client client) {
	    	
	    	 this.client = client;
	         setLayout(new BorderLayout(10, 10));
	         setBackground(Color.WHITE);
	         setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

	         JLabel title = new JLabel("Termin reservation", SwingConstants.CENTER);
	         title.setFont(new Font("Segoe UI", Font.BOLD, 18));
	         add(title, BorderLayout.NORTH);

	         JPanel form = new JPanel(new GridBagLayout());
	         form.setBackground(Color.WHITE);
	         form.setPreferredSize(new Dimension(400, 300));
	         GridBagConstraints gbc = new GridBagConstraints();
	         gbc.insets = new Insets(25, 8, 15, 8);
	         gbc.fill = GridBagConstraints.HORIZONTAL;

	         dateField = new JTextField(LocalDate.now().toString());
	         timeField = new JTextField("10:00");
	         serviceTypeBox = new JComboBox<>(new String[]{"Service", "Repair"});
	         descriptionArea = new JTextArea(4, 20);
	         
	         dateField.setPreferredSize(new Dimension(250, 30));
	         timeField.setPreferredSize(new Dimension(250, 30));
	         serviceTypeBox.setPreferredSize(new Dimension(250, 30));
	         descriptionArea.setPreferredSize(new Dimension(250, 80));
	         
	         dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	         timeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	         serviceTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	         descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

	         addField(form, gbc, 0, "Date (yyyy-MM-dd):", dateField);
	         addField(form, gbc, 1, "Time (HH:mm):", timeField);
	         addField(form, gbc, 2, "Type of service:", serviceTypeBox);
	         addField(form, gbc, 3, "Description:", new JScrollPane(descriptionArea));

	         JButton btnSave = new JButton("Save reservation");
	         btnSave.setBackground(new Color(52, 73, 94));
	         btnSave.setForeground(Color.BLACK);
	         btnSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	         btnSave.addActionListener(e -> saveAppointment());

	         add(form, BorderLayout.CENTER);
	         add(btnSave, BorderLayout.SOUTH);
	    	

	    }
	    
	    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, Component comp) {

	    	   	JLabel labelGui = new JLabel(label);
	    	   	labelGui.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    	    gbc.gridx = 0;
	    	    gbc.gridy = y;
	    	    gbc.weightx = 0; 
	    	    panel.add(labelGui, gbc);

	    	    gbc.gridx = 1;
	    	    gbc.weightx = 1.0; 
	    	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    	    panel.add(comp, gbc);
	    }
	    
	    private void saveAppointment() {
	        Appointment a = new Appointment();
	        a.setDate(dateField.getText());
	        a.setTime(timeField.getText());
	        a.setType((String) serviceTypeBox.getSelectedItem());
	        a.setDescription(descriptionArea.getText());
	        a.setOwnerUsername(client.getUsername());
	        service.createAppointment(a);
	        JOptionPane.showMessageDialog(this, "Appointment successfully scheduled", "Success", JOptionPane.INFORMATION_MESSAGE);
	    }
}
