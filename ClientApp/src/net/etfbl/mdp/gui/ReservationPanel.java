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
	        setLayout(new GridLayout(5, 2, 10, 10));
	        setBorder(BorderFactory.createTitledBorder("Termin reservation"));

	        dateField = new JTextField(LocalDate.now().toString());
	        timeField = new JTextField("10:00");
	        serviceTypeBox = new JComboBox<>(new String[]{"Service", "Reparation"});
	        descriptionArea = new JTextArea(3, 20);

	        JButton btnSchedule = new JButton("Save");
	        btnSchedule.addActionListener(e -> {
	            String msg = String.format(
	                    "Termin zakazan za %s u %s\nVrsta: %s\nOpis: %s",
	                    dateField.getText(),
	                    timeField.getText(),
	                    serviceTypeBox.getSelectedItem(),
	                    descriptionArea.getText()
	            );
	            Appointment appointment = new Appointment();
	            appointment.setDate(dateField.getText());
	            appointment.setTime(timeField.getText());
	            appointment.setType(serviceTypeBox.getSelectedItem().toString());
	            appointment.setDescription(descriptionArea.getText());
	            appointment.setOwnerUsername(client.getUsername());
	            JOptionPane.showMessageDialog(this, msg, "Potvrda", JOptionPane.INFORMATION_MESSAGE);
	            service.createAppointment(appointment);
	        });

	        add(new JLabel("Datum (yyyy-MM-dd):"));
	        add(dateField);
	        add(new JLabel("Vrijeme (HH:mm):"));
	        add(timeField);
	        add(new JLabel("Vrsta usluge:"));
	        add(serviceTypeBox);
	        add(new JLabel("Opis:"));
	        add(new JScrollPane(descriptionArea));
	        add(new JLabel());
	        add(btnSchedule);
	    }
}
