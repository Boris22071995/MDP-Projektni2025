package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.service.repository.ClientRepository;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.EmailSender;

import java.awt.*;
import java.awt.event.MouseEvent;
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
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InvoicePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();

	private JTextArea appointmentArea = new JTextArea(5, 50);
	private JTable partsTable;
	private DefaultTableModel partsModel;
	private RedisPartService redisService = new RedisPartService();
	private List<Part> invoiceItems = new ArrayList<>();
	private Appointment appointment;
	private ClientRepository clientRepo = new ClientRepository();

	public InvoicePanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Invoicing"));

		appointmentArea.setEditable(false);
		appointmentArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		add(appointmentArea, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

		center.add(new JSeparator());

		partsModel = new DefaultTableModel(new Object[] { "Id", "Name", "Price", "Quantity" }, 0);
		partsTable = new JTable(partsModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getToolTipText(MouseEvent e) {
				Point p =e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				
				if(row >=0 && col >=0) {
					Object value = getValueAt(row, col);
					if(value != null) {
						return value.toString();
					}
				}
				return null;
			}
		};
		center.add(new JScrollPane(partsTable));
		add(center, BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout());
		JButton btnGenerate = new JButton("Create Invoice");
		JButton btnSend = new JButton("Sent to Email");
		JButton btnAddPart = new JButton("Add part");

		for (JButton b : new JButton[] { btnGenerate, btnSend, btnAddPart }) {
			b.setFocusPainted(false);
			b.setFont(new Font("Segoe UI", Font.BOLD, 14));
			b.setBackground(new Color(52, 73, 94));
			b.setForeground(Color.BLACK);
		}

		btnAddPart.addActionListener(e -> addPartToInvoice());
		btnGenerate.addActionListener(e -> generateInvoice());
		btnSend.addActionListener(e -> sendInvoiceEmail());

		controls.add(btnAddPart);
		controls.add(btnGenerate);
		controls.add(btnSend);

		add(controls, BorderLayout.SOUTH);

	}

	File zipFileForSend = null;

	private void sendInvoiceEmail() {
		String clientName = appointment.getOwnerUsername();
		Client c = clientRepo.findByUsername(clientName);
		System.out.println(c.getEmail());
		try {
			EmailSender.sendZip("mdpService2025@gmail.com", "xorw ssob hcha mdhh", c.getEmail(),
					"Service invoice", "Dear customer,\n\nYour service invoice is attached.\n\nRegards.",
					zipFileForSend);

			JOptionPane.showMessageDialog(this, "Invoice successfully sent!");
			log.info("Invoice is sent.");

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error while sending email!");
			log.severe("Error while sending email.");
		}
	}

	private void loadParts() {
		partsModel.setRowCount(0);
		for (Part p : redisService.getAllParts()) {
			partsModel.addRow(new Object[] { p.getId(), p.getName(), p.getPrice() + "KM", p.getQuantity() });
		}
		log.info("Parts are loaded");
	}

	private void addPartToInvoice() {
		int row = partsTable.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select part first!");
			log.warning("Selection of part is needed.");
			return;
		}

		String id = partsModel.getValueAt(row, 0).toString();
		Part p = redisService.getPart(id);

		if (p.getQuantity() <= 0) {
			JOptionPane.showMessageDialog(this, "There are no more parts!");
			log.warning("There are no more parts.");
			return;
		}

		invoiceItems.add(p);

		p.setQuantity(p.getQuantity() - 1);
		if (p.getQuantity() == 0) {
			redisService.deletePart(id);
		} else {
			redisService.updatePart(p);
		}

		loadParts();
		log.info("Part is added to invoice.");
		JOptionPane.showMessageDialog(this, "Part added to invoice!");
	}

	public void loadAppointment(Appointment a) {
		appointment = a;
		appointmentArea.setText("Client: " + a.getOwnerUsername() + "\n" + "Date: " + a.getDate() + "\n" + "Time: "
				+ a.getTime() + "\n" + "Type: " + a.getType());
		loadParts();
		log.info("Appointments loaded.");
		invoiceItems.clear();
	}

	public void generateInvoice() {

		if (appointment == null) {
			JOptionPane.showMessageDialog(this, "No appointment selected!");
			log.warning("No appointment selected for invoice.");
			return;
		}
		if (invoiceItems.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No parts added to invoice!");
			log.warning("No part added for invoice.");
			return;
		}

		try {
			String fileName = buildFileName();
			File txtFile = new File(fileName);

			writeInvoiceToFile(txtFile);
			File zipFile = zipInvoice(txtFile);

			JOptionPane.showMessageDialog(this, "Invoice generated successfully! " + zipFile.getName());
			log.info("Invoice is generated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No appointment selected!");
			log.severe("No appointment selected.");
			return;
		}
	}

	private File zipInvoice(File txtFile) throws IOException {
		String zipName = txtFile.getName().replace(".txt", ".zip");
		File zipFile = new File(zipName);

		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
			FileInputStream fis = new FileInputStream(txtFile)) {

			ZipEntry entry = new ZipEntry(txtFile.getName());
			zos.putNextEntry(entry);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}

			zos.closeEntry();
		}catch(Exception e) {
			log.severe("Error while creating zip file.");
		}
		zipFileForSend = zipFile;
		return zipFile;
	}

	private String buildFileName() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMDD_HHmm");
		String timeStamp = LocalDateTime.now().format(formatter);
		String username = appointment.getOwnerUsername();

		return username + "_" + timeStamp + ".txt";
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
				writer.write(p.getName() + " | " + p.getPrice() + " KM");
				writer.newLine();
				total += p.getPrice();
			}

			writer.newLine();
			writer.write("----------------------");
			writer.newLine();
			writer.write("TOTAL: " + String.format("%.2f", total) + " KM");
			writer.newLine();

			writer.newLine();
			writer.write("Thank you for using our service.");
		}

	}
}
