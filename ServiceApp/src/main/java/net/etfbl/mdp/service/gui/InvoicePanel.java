package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.util.EmailSender;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InvoicePanel extends JPanel {

	private JTextArea appointmentArea = new JTextArea(5,50);
	private JTable partsTable;
	private DefaultTableModel partsModel;
	private RedisPartService redisService = new RedisPartService();
	private List<Part> invoiceItems = new ArrayList<>();
	private Appointment appointment;
	public InvoicePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Invoicing"));
        
        appointmentArea.setEditable(false);
        appointmentArea.setFont(new Font("Monospaced",Font.PLAIN,13));
        add(appointmentArea,BorderLayout.NORTH);
        
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        
        center.add(new JSeparator());
        
        partsModel = new DefaultTableModel(new Object[] {"Id", "Name", "Price","Quantity"},0);
        partsTable = new JTable(partsModel);
        center.add(new JScrollPane(partsTable));
        add(center,BorderLayout.CENTER);

//        JTextArea invoicePreview = new JTextArea();
//        invoicePreview.setEditable(false);
//        invoicePreview.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel controls = new JPanel(new FlowLayout());
        JButton btnGenerate = new JButton("Create Invoice");
        JButton btnSend = new JButton("Sent to Email");
        JButton btnAddPart = new JButton("Add part");
        
        for (JButton b : new JButton[]{btnGenerate, btnSend, btnAddPart}) {
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setBackground(new Color(52, 73, 94));
            b.setForeground(Color.BLACK);
        }
        
        //  btnInvoice.addActionListener(e -> cardLayout.show(mainPanel, "invoice"));
        btnAddPart.addActionListener(e -> addPartToInvoice());
        btnGenerate.addActionListener(e -> generateInvoice());
        btnSend.addActionListener(e -> sendInvoiceEmail());
        
        controls.add(btnAddPart);
        controls.add(btnGenerate);
        controls.add(btnSend);

       // add(new JScrollPane(invoicePreview), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

       
    }
	
	File zipFileForSend = null;
	
	private void sendInvoiceEmail() {
		 try {
		       // File zipFile = getLastGeneratedZip(); // ti već imaš zip

		        EmailSender.sendZip(
		            "mdpService2025@gmail.com",
		            "xorw ssob hcha mdhh",
		            "bvasiljevic21@gmail.com",
		            "Service invoice",
		            "Dear customer,\n\nYour service invoice is attached.\n\nRegards.",
		            zipFileForSend
		        );

		        JOptionPane.showMessageDialog(this,
		                "📧 Invoice successfully sent!");

		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this,
		                "Error sending email!");
		    }
	}

	private void loadParts() {
		partsModel.setRowCount(0);
		for(Part p : redisService.getAllParts()) {
			partsModel.addRow(new Object[] {p.getId(), p.getName(), p.getPrice(), p.getQuantity()});
		}
	}
	
	private void addPartToInvoice() {
		int row = partsTable.getSelectedRow();
		if(row < 0) {
			JOptionPane.showMessageDialog(this, "Select part first!");
			return;
		}
		
		String id = partsModel.getValueAt(row, 0).toString();
		Part p = redisService.getPart(id);
		
		if(p.getQuantity() <= 0) {
			JOptionPane.showMessageDialog(this,"Out of stock!");
			return;
		}
		
		invoiceItems.add(p);
		
		p.setQuantity(p.getQuantity() - 1);
		if(p.getQuantity() == 0) {
			redisService.deletePart(id);
		}else {
			redisService.updatePart(p);
		}
		
		loadParts();
		JOptionPane.showMessageDialog(this,"Part added to invoice!");
	}
	
	public void loadAppointment(Appointment a) {
		appointment = a;
		appointmentArea.setText("Client: " + a.getOwnerUsername() + "\n" + "Date: " + a.getDate() + "\n" + "Time: " + a.getTime() + "\n" + "Type: " + a.getType());
		loadParts();
		invoiceItems.clear();
	}
	
	
	
	public void generateInvoice() {
		
		if(appointment == null) {
			JOptionPane.showMessageDialog(this, "No appointment selected!");
			return;
		}
		if(invoiceItems.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No parts added to invoice!");
			return;
		}
		
		try {
			String fileName = buildFileName();
			File txtFile = new File(fileName);
			
			writeInvoiceToFile(txtFile);
			File zipFile = zipInvoice(txtFile);
			
			JOptionPane.showMessageDialog(this, "Invoice generated successfully! " + zipFile.getName());
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No appointment selected!");
			return;
		}
	}

	private File zipInvoice(File txtFile) throws IOException{
		String zipName = txtFile.getName().replace(".txt", ".zip");
	    File zipFile = new File(zipName);

	    try (ZipOutputStream zos =
	                 new ZipOutputStream(new FileOutputStream(zipFile));
	         FileInputStream fis =
	                 new FileInputStream(txtFile)) {

	        ZipEntry entry = new ZipEntry(txtFile.getName());
	        zos.putNextEntry(entry);

	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = fis.read(buffer)) > 0) {
	            zos.write(buffer, 0, length);
	        }

	        zos.closeEntry();
	    }
	    zipFileForSend = zipFile;
	    return zipFile;
	}

	private String buildFileName() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMDD_HHmm");
		String timeStamp = LocalDateTime.now().format(formatter);
		String username = appointment.getOwnerUsername();
		
		return username+"_"+timeStamp+".txt";
	}

	private void writeInvoiceToFile(File file) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

	        writer.write("===== SERVICE INVOICE =====");
	        writer.newLine();
	        writer.newLine();

	        writer.write("Client: " + appointment.getOwnerUsername());
	        writer.newLine();

	        writer.write("Service type: " + appointment.getType());
	        writer.newLine();

	        writer.write("Date: " + appointment.getDate());
	        writer.newLine();

	        writer.write("Time: " + appointment.getTime());
	        writer.newLine();

	        writer.newLine();
	        writer.write("----- Used parts -----");
	        writer.newLine();

	        double total = 0;

	        for (Part p : invoiceItems) {
	            writer.write(
	                p.getName() + " | " +
	                p.getPrice() + " EUR"
	            );
	            writer.newLine();
	            total += p.getPrice();
	        }

	        writer.newLine();
	        writer.write("----------------------");
	        writer.newLine();
	        writer.write("TOTAL: " + String.format("%.2f", total) + " EUR");
	        writer.newLine();

	        writer.newLine();
	        writer.write("Thank you for using our service.");
	    }
		
	}
}
