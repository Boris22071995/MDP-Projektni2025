package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AppointmentService;
import net.etfbl.mdp.util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class ReservationPanel extends JPanel {
	
	private static final Logger log = AppLogger.getLogger();

    private JSpinner dateSpinner;    // NEW
    private JSpinner timeSpinner;    // NEW
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

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(250, 30));

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(250, 30));

        serviceTypeBox = new JComboBox<>(new String[]{"Service", "Repair"});
        descriptionArea = new JTextArea(4, 20);

        serviceTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        addField(form, gbc, 0, "Date:", dateSpinner);
        addField(form, gbc, 1, "Time:", timeSpinner);
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

       
        Date dateVal = (Date) dateSpinner.getValue();
        LocalDate date = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Extract LocalTime from spinner
        Date timeVal = (Date) timeSpinner.getValue();
        LocalTime time = timeVal.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

        if (date.isBefore(LocalDate.now())) {
        	log.warning("Date cannot be in the past.");
            JOptionPane.showMessageDialog(this,
                    "Date cannot be in the past.",
                    "Invalid date",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(16, 0);
        
        
        if (time.isBefore(start) || time.isAfter(end)) {
        	log.warning("Reservation must be between 08:00 and 16:00.");
            JOptionPane.showMessageDialog(this,
                    "Reservation must be between 08:00 and 16:00.",
                    "Invalid time",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Appointment a = new Appointment();
        a.setDate(date.toString());
        a.setTime(time.toString().substring(0, 5)); 
        a.setType((String) serviceTypeBox.getSelectedItem());
        a.setDescription(descriptionArea.getText());
        a.setOwnerUsername(client.getUsername());

        service.createAppointment(a);
        log.info("Appointment successfullu scheduled.");
        JOptionPane.showMessageDialog(this,
                "Appointment successfully scheduled",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        
        dateSpinner.setValue(new Date());

        // Reset time to default start time 08:00
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        timeSpinner.setValue(cal.getTime());

        // Reset combo box
        serviceTypeBox.setSelectedIndex(0);

        // Clear description
        descriptionArea.setText("");
    }
}



//package net.etfbl.mdp.gui;
//
//import net.etfbl.mdp.model.Appointment;
//import net.etfbl.mdp.model.Client;
//import net.etfbl.mdp.service.AppointmentService;
//
//import javax.swing.*;
//import java.awt.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//public class ReservationPanel extends JPanel {
//
//	 private JTextField dateField;
//	    private JTextField timeField;
//	    
//	    private JSpinner dateSpinner;
//	    private JSpinner timeSpinner;
//	    
//	    private JComboBox<String> serviceTypeBox;
//	    private JTextArea descriptionArea;
//	    AppointmentService service = new AppointmentService();
//	    Client client = new Client();
//
//	    public ReservationPanel(Client client) {
//	    	
//	    	 this.client = client;
//	         setLayout(new BorderLayout(10, 10));
//	         setBackground(Color.WHITE);
//	         setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
//
//	         JLabel title = new JLabel("Termin reservation", SwingConstants.CENTER);
//	         title.setFont(new Font("Segoe UI", Font.BOLD, 18));
//	         add(title, BorderLayout.NORTH);
//
//	         JPanel form = new JPanel(new GridBagLayout());
//	         form.setBackground(Color.WHITE);
//	         form.setPreferredSize(new Dimension(400, 300));
//	         GridBagConstraints gbc = new GridBagConstraints();
//	         gbc.insets = new Insets(25, 8, 15, 8);
//	         gbc.fill = GridBagConstraints.HORIZONTAL;
//
//	         dateField = new JTextField(LocalDate.now().toString());
//	         timeField = new JTextField("10:00");
//	         serviceTypeBox = new JComboBox<>(new String[]{"Service", "Repair"});
//	         descriptionArea = new JTextArea(4, 20);
//	         
//	         dateField.setPreferredSize(new Dimension(250, 30));
//	         timeField.setPreferredSize(new Dimension(250, 30));
//	         serviceTypeBox.setPreferredSize(new Dimension(250, 30));
//	         descriptionArea.setPreferredSize(new Dimension(250, 80));
//	         
//	         dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//	         timeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//	         serviceTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//	         descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//
//	         addField(form, gbc, 0, "Date (yyyy-MM-dd):", dateField);
//	         addField(form, gbc, 1, "Time (HH:mm):", timeField);
//	         addField(form, gbc, 2, "Type of service:", serviceTypeBox);
//	         addField(form, gbc, 3, "Description:", new JScrollPane(descriptionArea));
//
//	         JButton btnSave = new JButton("Save reservation");
//	         btnSave.setBackground(new Color(52, 73, 94));
//	         btnSave.setForeground(Color.BLACK);
//	         btnSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//	         btnSave.addActionListener(e -> saveAppointment());
//
//	         add(form, BorderLayout.CENTER);
//	         add(btnSave, BorderLayout.SOUTH);
//	    	
//
//	    }
//	    
//	    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, Component comp) {
//
//	    	   	JLabel labelGui = new JLabel(label);
//	    	   	labelGui.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//	    	    gbc.gridx = 0;
//	    	    gbc.gridy = y;
//	    	    gbc.weightx = 0; 
//	    	    panel.add(labelGui, gbc);
//
//	    	    gbc.gridx = 1;
//	    	    gbc.weightx = 1.0; 
//	    	    gbc.fill = GridBagConstraints.HORIZONTAL;
//	    	    panel.add(comp, gbc);
//	    }
//	    
//	    private void saveAppointment() {
//	        Appointment a = new Appointment();
//	        a.setDate(dateField.getText());
//	        a.setTime(timeField.getText());
//	        a.setType((String) serviceTypeBox.getSelectedItem());
//	        a.setDescription(descriptionArea.getText());
//	        a.setOwnerUsername(client.getUsername());
//	        service.createAppointment(a);
//	        JOptionPane.showMessageDialog(this, "Appointment successfully scheduled", "Success", JOptionPane.INFORMATION_MESSAGE);
//	    }
//}
