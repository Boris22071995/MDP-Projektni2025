package net.etfbl.mdp.service.gui;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import net.etfbl.mdp.messaging.OrderPublisher;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.model.Part;

public class SupplierOrdersPanel extends JPanel {
	 private JComboBox<String> supplierBox;
	    private JTable table;
	    private DefaultTableModel model;

	    public SupplierOrdersPanel() {
	        setLayout(new BorderLayout(10, 10));
	        setBorder(BorderFactory.createTitledBorder("🔧 Parts ordering from suppliers"));

	        // top panel — izbor dobavljača
	        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        supplierBox = new JComboBox<>(new String[]{"Supplier1", "Supplier2", "Supplier3", "Supplier4"});
	        JButton btnLoad = new JButton("Load parts");
	        topPanel.add(new JLabel("Select supplier:"));
	        topPanel.add(supplierBox);
	        topPanel.add(btnLoad);
	        add(topPanel, BorderLayout.NORTH);

	        // tabela dijelova
	        model = new DefaultTableModel(new Object[]{"Code", "Name", "Price", "Image URL"}, 0);
	        table = new JTable(model);
	        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        add(new JScrollPane(table), BorderLayout.CENTER);

	        // bottom panel — narudžbe
	        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
	        JButton btnOrder = new JButton("Send order");
	        JButton btnRefresh = new JButton("Refresh");
	        for (JButton b : new JButton[]{btnOrder, btnRefresh, btnLoad}) {
	            b.setFocusPainted(false);
	            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
	            b.setBackground(new Color(41, 128, 185));
	            b.setForeground(Color.WHITE);
	        }

	        bottomPanel.add(btnOrder);
	        bottomPanel.add(btnRefresh);
	        add(bottomPanel, BorderLayout.SOUTH);

	        // akcije
	        btnLoad.addActionListener(e -> {
				try {
					loadPartsFromSupplier();
				} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
						| IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnOrder.addActionListener(e -> sendOrder());
	        btnRefresh.addActionListener(e -> refreshList());
	    }

	    private void loadPartsFromSupplier() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
	    	  String TRUST_STORE_PATH = "./clienttruststore.jks";
    	      String TRUST_STORE_PASSWORD = "trustpass";
    	      File f = new File(TRUST_STORE_PATH);
    	      TRUST_STORE_PATH = f.getAbsolutePath();
	        String supplier = (String) supplierBox.getSelectedItem();
	        int port = 5001; // default
	        if ("Supplier1".equalsIgnoreCase(supplier)) {
	            port = 5001;
	        } else if ("Supplier2".equalsIgnoreCase(supplier)) {
	            port = 5002;
	        } else if ("Supplier3".equalsIgnoreCase(supplier)) {
	            port = 5003;
	        } else if ("Supplier4".equalsIgnoreCase(supplier)) {
	            port = 5004;
	        }
	        System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
            System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);
            char[] passphrase = TRUST_STORE_PASSWORD.toCharArray();
            KeyStore ts = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(TRUST_STORE_PATH)) {
                ts.load(fis, passphrase);
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sf = sc.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket("localhost", port);
            socket.startHandshake(); 
           
	        List<Part> parts = new ArrayList<>();
	        BufferedReader   in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter  out = new PrintWriter(socket.getOutputStream(), true);
	        Thread reciverThread = new Thread(()->{
	        	
	        	try {
	        		out.println("GET_PARTS|" + supplier);
	        		 
	        		String line;
	        		while((line = in.readLine()) != null) {
	        			if ("END".equals(line)) break;
	        			if(line.startsWith("PART|")) {
	        				 String[] p = line.split("\\|", 5);
	 	                    if (p.length >= 5) {
	 	                        parts.add(new Part(
	 	                                decode(p[1]),
	 	                                decode(p[2]),
	 	                                Double.parseDouble(p[3]),
	 	                                decode(p[4])
	 	                        ));
	 	                    }
	        			}
	        		}
	        		model.setRowCount(0);
	        		 for (Part part : parts) {
	     	            model.addRow(new Object[]{
	     	                    part.getId(), part.getName(), part.getPrice(), part.getDescription()
	     	            });
	     	        }
	        		 JOptionPane.showMessageDialog(this, "✅ Loaded " + parts.size() + " parts from " + supplier);
	        		
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        		JOptionPane.showMessageDialog(this, "❌ Error loading parts: " + e.getMessage(),
		                    "Error", JOptionPane.ERROR_MESSAGE);
	        	}
	        });
	        reciverThread.start();
//	        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", port);
//	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//	             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {
//	        	socket.setEnabledProtocols(new String[] {"TLSv1.2"});
//	        	socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
//	        	System.out.println("EVO NAS OVDJE NA SERVISU A DOBAVLJAC");
//	            out.println("GET_PARTS|" + supplier);
//
//	            String line;
//	            while ((line = in.readLine()) != null) {
//	            	
//	                if ("END".equals(line)) break;
//	                if (line.startsWith("PART|")) {
//	                    String[] p = line.split("\\|", 5);
//	                    if (p.length >= 5) {
//	                        parts.add(new Part(
//	                                decode(p[1]),
//	                                decode(p[2]),
//	                                Double.parseDouble(p[3]),
//	                                decode(p[4])
//	                        ));
//	                    }
//	                }
//	            }
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            JOptionPane.showMessageDialog(this, "❌ Error loading parts: " + e.getMessage(),
//	                    "Error", JOptionPane.ERROR_MESSAGE);
//	        }

	        // popuni tabelu
//	        model.setRowCount(0);
//	        for (Part part : parts) {
//	            model.addRow(new Object[]{
//	                    part.getId(), part.getName(), part.getPrice(), part.getDescription()
//	            });
//	        }

//	        JOptionPane.showMessageDialog(this, "✅ Loaded " + parts.size() + " parts from " + supplier);
	    }

	    private void sendOrder() {
	        int row = table.getSelectedRow();
	        if (row < 0) {
	            JOptionPane.showMessageDialog(this, "Please select a part first.", "Warning", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        String code = (String) model.getValueAt(row, 0);
	        String supplier = (String) supplierBox.getSelectedItem();
	        String partName = (String) model.getValueAt(row, 1);
	        Double price = (Double) model.getValueAt(row, 2);
	        String qty = JOptionPane.showInputDialog(this, "Enter quantity for part " + code + ":");
	        Integer quant = Integer.parseInt(qty);
	        //System.out.println("NARUDZBA: " + code + " " + supplier + " " + partName + " " + price + " " + quant);
	        //String orderId, String clientUsername, String partName, int quantity, double price
	       
	        Order order = new Order(code, supplier, partName, quant, price);
	      //  System.out.println("NARUDZBA: " + order);
	        if (qty == null || qty.isEmpty()) return;
	        String queueName = "";
	        if("Supplier1".equals(supplier)) {
	        	queueName = "orders_queue1";
	        }else if ("Supplier2".equals(supplier)) {
	        	queueName = "orders_queue2";
	        }else if("Supplier3".equals(supplier)) {
	        	queueName = "orders_queue3";
	        }else {
	        	queueName = "orders_queue4";
	        }
	        
	        OrderPublisher.sendOrder(order, queueName, quant);
	  //      System.out.println("NARUDZBA: " + order);
	       // OrderPublisher.sendOrder(null, qty, row)
	        // TODO: ovdje ide RabbitMQ publish logika (slanje zahtjeva u red)
	   //     JOptionPane.showMessageDialog(this, "📦 Order for part " + code +
	  //              " sent to " + supplier + " (qty: " + qty + ")", "Order sent", JOptionPane.INFORMATION_MESSAGE);
	    }

	    private void refreshList() {
	        model.setRowCount(0);
	    }

	    // Pomocne metode za kodiranje/dekodiranje (ako koristiš | u imenima)
	    private static String decode(String val) {
	        return val.replace("%7C", "|");
	    }
}
