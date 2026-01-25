package net.etfbl.mdp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

import net.etfbl.mdp.messaging.OrderPublisher;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class SupplierOrderService {
	
	private static final Logger log = AppLogger.getLogger();

	public List<Part> loadParts(String supplierName) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
		String TRUST_STORE_PATH = ConfigurationLoader.getString("truststore.path");
		String TRUST_STORE_PASSWORD = ConfigurationLoader.getString("truststire.password");
		File f = new File(TRUST_STORE_PATH);
		TRUST_STORE_PATH = f.getAbsolutePath();
		String supplier =  supplierName;
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

		List<Part> parts = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		try {
			out.println("GET_PARTS|" + supplier);

			String line;
			while ((line = in.readLine()) != null) {
				if ("END".equals(line))
					break;
				if (line.startsWith("PART|")) {
					String[] p = line.split("\\|", 5);
					if (p.length >= 5) {
						parts.add(new Part(decode(p[1]), decode(p[2]), Double.parseDouble(p[3]), decode(p[4])));
					}
				}
			}
			log.info("Parts from supplier are loaded.");
			return parts;
		

		} catch (Exception e) {
			e.printStackTrace();
			log.severe("Error while loading parts from supplier.");
		}
		
		socket.close();		
		return parts;
	}

	
	private static String decode(String s) {
		  try {
				return s == null ? "" : URLDecoder.decode(s, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return s;
			}
	}
}
