package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Appointment.Status;
import net.etfbl.mdp.model.Appointment.Type;
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

	    public ReservationPanel() {
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
	            JOptionPane.showMessageDialog(this, msg, "Potvrda", JOptionPane.INFORMATION_MESSAGE);
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
