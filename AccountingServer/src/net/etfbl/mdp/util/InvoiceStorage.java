package net.etfbl.mdp.util;

import net.etfbl.mdp.model.Invoice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceStorage {
	
	private static final String FILE_NAME = "invoices.ser";
	
	public static synchronized void saveInvoice(List<Invoice> invoices) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
			oos.writeObject(invoices);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized List<Invoice> loadInvoices() {
		File f = new File(FILE_NAME);
		if(!f.exists()) return new ArrayList<>();
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
			return (List<Invoice>) ois.readObject();
		}catch(Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
