package net.etfbl.mdp.service.gui;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.etfbl.mdp.messaging.OrderPublisher;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.service.SupplierOrderService;
import net.etfbl.mdp.service.gui.rest.RestSupplierOrder;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class SupplierOrdersPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();

	private JComboBox<String> supplierBox;
	private JTable table;
	private DefaultTableModel model;
	private JTable ordersTable;
	private DefaultTableModel ordersModel;
	private RedisPartService redisService;
	private SupplierOrderService orderService = new SupplierOrderService();
	private RestSupplierOrder restSupplier;
	
	public SupplierOrdersPanel(RedisPartService service) {
		this.restSupplier = new RestSupplierOrder();
		this.redisService = service;
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createTitledBorder("Parts ordering from suppliers"));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		supplierBox = new JComboBox<>(new String[] { "Supplier1", "Supplier2", "Supplier3", "Supplier4" });
		JButton btnLoad = new JButton("Load parts");
		topPanel.add(new JLabel("Select supplier:"));
		topPanel.add(supplierBox);
		topPanel.add(btnLoad);
		add(topPanel, BorderLayout.NORTH);

		model = new DefaultTableModel(new Object[] { "Code", "Name", "Price", "Image URL" }, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int colum) {
				return false;
			}
		};
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getToolTipText(MouseEvent e) {
				Point p = e.getPoint();
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
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2) {
        			int row = table.getSelectedRow();
        			int col = table.getSelectedColumn();      			
        			if(row >= 0 && col == 3) {
        				String imageUrl = table.getValueAt(row, col).toString();
        				showImageDialog(imageUrl);
        			}
        		}
        	}
        });
		
		ordersModel = new DefaultTableModel(
			    new Object[]{"Supplier","Code", "Part", "Qty", "Price(KM)", "Status"}, 0
			);
			ordersTable = new JTable(ordersModel) {
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getToolTipText(MouseEvent e) {
					Point p = e.getPoint();
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
			};;
			ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), new JScrollPane(ordersTable));
			splitPane.setDividerLocation(300);
			add(splitPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		JButton btnOrder = new JButton("Send order");
		JButton btnRefresh = new JButton("Refresh");
		JButton btnCheckSts = new JButton("Check status");
		
		for (JButton b : new JButton[] { btnOrder, btnRefresh, btnLoad, btnCheckSts }) {
			b.setFocusPainted(false);
			b.setFont(new Font("Segoe UI", Font.BOLD, 13));
			b.setBackground(new Color(52, 73, 94));
			b.setForeground(Color.BLACK);
		}

		bottomPanel.add(btnOrder);
		bottomPanel.add(btnRefresh);
		bottomPanel.add(btnCheckSts);
		add(bottomPanel, BorderLayout.SOUTH);

		btnLoad.addActionListener(e -> {
			try {
				loadPartsFromSupplier();
			} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
					| IOException e1) {
				e1.printStackTrace();
			}
		});
		btnOrder.addActionListener(e -> sendOrder());
		btnRefresh.addActionListener(e -> refreshList());
		btnCheckSts.addActionListener(e -> {
			try {
				checkStatus();
			} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
					| IOException e1) {
				e1.printStackTrace();
			}
		});
		
	}

	private void checkStatus()  throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
		
		int row = ordersTable.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select appointment first!");
			log.warning("Selection of appointment is needed.");
			return;
		}
		String orderId = ordersModel.getValueAt(row,1).toString();	
		String TRUST_STORE_PATH = ConfigurationLoader.getString("truststore.path");
		String TRUST_STORE_PASSWORD = ConfigurationLoader.getString("truststire.password");
		File f = new File(TRUST_STORE_PATH);
		TRUST_STORE_PATH = f.getAbsolutePath();
		String supplier = (String) supplierBox.getSelectedItem();
		int port = ConfigurationLoader.getInt("supplierone.port");
		if (ConfigurationLoader.getString("supplierone.name").equalsIgnoreCase(supplier)) {
			port = ConfigurationLoader.getInt("supplierone.port");
		} else if (ConfigurationLoader.getString("suppliertwo.name").equalsIgnoreCase(supplier)) {
			port = ConfigurationLoader.getInt("suppliertwo.port");
		} else if (ConfigurationLoader.getString("supplierthree.name").equalsIgnoreCase(supplier)) {
			port = ConfigurationLoader.getInt("supplierthree.port");
		} else if (ConfigurationLoader.getString("supplierfour.name").equalsIgnoreCase(supplier)) {
			port = ConfigurationLoader.getInt("supplierfour.port");
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

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	
		try {
			out.println("STATUS|" + orderId);
			String status = "PENDING";
			String line;
			while ((line = in.readLine()) != null) {
				if ("END".equals(line))
					break;
				if (line.startsWith("STATUS|")) {
					String[] p = line.split("\\|");
					status = p[1];
				}
			}
			
			if("APPROVED".equals(status)) {
				Part p = new Part(decode(ordersModel.getValueAt(row, 1).toString()),decode(ordersModel.getValueAt(row, 0).toString()), decode(ordersModel.getValueAt(row, 2).toString()),Double.parseDouble(ordersModel.getValueAt(row, 4).toString()),Integer.parseInt(ordersModel.getValueAt(row, 3).toString()),"Description");
				redisService.addPart(p);
				log.info("Status of order is approved.");
			}
			if(status == null || status.equalsIgnoreCase("null")) {status = "PENDING";}
			 ordersModel.setValueAt(status, row, 5);		

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading parts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			log.severe("Error in loading parts.");
		}
		
		socket.close();		
	}

	
	private void loadPartsFromSupplier() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
		
		String supplier = (String) supplierBox.getSelectedItem();
		try {
			List<Part> parts = orderService.loadParts(supplier);
			model.setRowCount(0);
			for (Part part : parts) {
				model.addRow(new Object[] { part.getId(), part.getName(), part.getPrice(), part.getDescription() });
			}
			JOptionPane.showMessageDialog(this, "Loaded " + parts.size() + " parts from " + supplier);
			log.info("Parts from supplier are loaded.");
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading parts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			log.severe("Error while loading parts from supplier.");
		}
		
	}

//	private void loadPartsFromSupplier() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
//			
//		String TRUST_STORE_PATH = ConfigurationLoader.getString("truststore.path");
//		String TRUST_STORE_PASSWORD = ConfigurationLoader.getString("truststire.password");
//		File f = new File(TRUST_STORE_PATH);
//		TRUST_STORE_PATH = f.getAbsolutePath();
//		String supplier = (String) supplierBox.getSelectedItem();
//		int port = ConfigurationLoader.getInt("supplierone.port");
//		if (ConfigurationLoader.getString("supplierone.name").equalsIgnoreCase(supplier)) {
//			port = ConfigurationLoader.getInt("supplierone.port");
//		} else if (ConfigurationLoader.getString("suppliertwo.name").equalsIgnoreCase(supplier)) {
//			port = ConfigurationLoader.getInt("suppliertwo.port");
//		} else if (ConfigurationLoader.getString("supplierthree.name").equalsIgnoreCase(supplier)) {
//			port = ConfigurationLoader.getInt("supplierthree.port");
//		} else if (ConfigurationLoader.getString("supplierfour.name").equalsIgnoreCase(supplier)) {
//			port = ConfigurationLoader.getInt("supplierfour.port");
//		}
//		System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
//		System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);
//		char[] passphrase = TRUST_STORE_PASSWORD.toCharArray();
//		KeyStore ts = KeyStore.getInstance("JKS");
//		try (FileInputStream fis = new FileInputStream(TRUST_STORE_PATH)) {
//			ts.load(fis, passphrase);
//		}
//
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//		tmf.init(ts);
//
//		SSLContext sc = SSLContext.getInstance("TLS");
//		sc.init(null, tmf.getTrustManagers(), null);
//
//		SSLSocketFactory sf = sc.getSocketFactory();
//		SSLSocket socket = (SSLSocket) sf.createSocket("localhost", port);
//		socket.startHandshake();
//
//		List<Part> parts = new ArrayList<>();
//		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//		
//		try {
//			out.println("GET_PARTS|" + supplier);
//
//			String line;
//			while ((line = in.readLine()) != null) {
//				if ("END".equals(line))
//					break;
//				if (line.startsWith("PART|")) {
//					String[] p = line.split("\\|", 5);
//					if (p.length >= 5) {
//						parts.add(new Part(decode(p[1]), decode(p[2]), Double.parseDouble(p[3]), decode(p[4])));
//					}
//				}
//			}
//			model.setRowCount(0);
//			for (Part part : parts) {
//				model.addRow(new Object[] { part.getId(), part.getName(), part.getPrice(), part.getDescription() });
//			}
//			JOptionPane.showMessageDialog(this, "Loaded " + parts.size() + " parts from " + supplier);
//			log.info("Parts from supplier are loaded.");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(this, "Error loading parts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//			log.severe("Error while loading parts from supplier.");
//		}
//		
//		socket.close();		
//	}

	private void sendOrder() {
		
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Please select a part first.", "Warning", JOptionPane.WARNING_MESSAGE);
			log.warning("Selection of part is needed.");
			return;
		}

		String code = (String) model.getValueAt(row, 0);
		String supplier = (String) supplierBox.getSelectedItem();
		String partName = (String) model.getValueAt(row, 1);
		Double price = (Double) model.getValueAt(row, 2);
		String qty = JOptionPane.showInputDialog(this, "Enter quantity for part " + code + ":");
		Integer quant = Integer.parseInt(qty);

		Order order = new Order(code, supplier, partName, quant, price);
		if (qty == null || qty.isEmpty())
			return;
		String queueName = "";
		if (ConfigurationLoader.getString("supplierone.name").equals(supplier)) {
			queueName = ConfigurationLoader.getString("supplierone.mqname");
		} else if (ConfigurationLoader.getString("suppliertwo.name").equals(supplier)) {
			queueName = ConfigurationLoader.getString("suppliertwo.mqname");
		} else if (ConfigurationLoader.getString("supplierthree.name").equals(supplier)) {
			queueName = ConfigurationLoader.getString("supplierthree.mqname");
		} else {
			queueName = ConfigurationLoader.getString("supplierfour.mqname");
		}

		OrderPublisher.sendOrder(order, queueName, quant);
		log.info("Order to publisher is sent.");
		ordersModel.addRow(new Object[] {supplier,code, partName, quant, price, "PENDING"});

	}

	private void refreshList() {
		model.setRowCount(0);
	}

	private static String decode(String s) {
		  try {
				return s == null ? "" : URLDecoder.decode(s, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return s;
			}
	}
	
	private void showImageDialog(String imageUrl) {
			
		 try {
	            TrustManager[] trustAllCertificates = new TrustManager[]{
	                    new X509TrustManager() {
	                        public X509Certificate[] getAcceptedIssuers() {
	                            return null;
	                        }

	                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

	                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	                    }
	            };

	            SSLContext sc = SSLContext.getInstance("TLS");
	            sc.init(null, trustAllCertificates, new java.security.SecureRandom());
	            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

	        } catch (Exception e) {
	        	log.severe("Error while loading image.");
	            e.printStackTrace();
	        }

	        JDialog dialog = new JDialog(
	                SwingUtilities.getWindowAncestor(this), "Part image", Dialog.ModalityType.APPLICATION_MODAL);
	        dialog.setSize(600, 500);
	        dialog.setLocationRelativeTo(this);

	        JLabel imageLabel = new JLabel("Loading image...", JLabel.CENTER);
	        JScrollPane scrollPane = new JScrollPane(imageLabel);
	        dialog.add(scrollPane);

	        new Thread(() -> {
	            try {
	                URL url = new URL(imageUrl);
	                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	                connection.setRequestMethod("GET");
	                connection.setConnectTimeout(5000); 
	                connection.setReadTimeout(5000); 

	                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                	log.severe("Failed to connect to URL");
	                    throw new IOException("Failed to connect to URL: " + connection.getResponseCode());
	                }

	                ImageIcon icon = new ImageIcon(url);
	                Image img = icon.getImage();

	                if (img == null || img.getWidth(null) == -1 || img.getHeight(null) == -1) {
	                	log.severe("Failed to load image.");
	                    throw new IOException("Image not loaded properly");
	                }

	                Image scaled = img.getScaledInstance(600, 450, Image.SCALE_SMOOTH);

	                SwingUtilities.invokeLater(() -> {
	                    imageLabel.setText(null);
	                    imageLabel.setIcon(new ImageIcon(scaled));
	                });

	            } catch (Exception e) {
	                SwingUtilities.invokeLater(() -> {
	                	log.severe("Failed to load image.");
	                    imageLabel.setText("Failed to load image. Error: " + e.getMessage());
	                });
	            }
	        }).start();

	        dialog.setVisible(true);
		
	}
}
