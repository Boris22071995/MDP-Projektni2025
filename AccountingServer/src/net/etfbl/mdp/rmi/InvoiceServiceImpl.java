package net.etfbl.mdp.rmi;

import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;
import net.etfbl.mdp.util.InvoiceStorage;

import java.io.File;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();
	private List<Invoice> invoices;
	
	private static final String INVOICE_DIR = ConfigurationLoader.getString("invoice.dir");
	
	public InvoiceServiceImpl() throws RemoteException {
		super();
		new File(INVOICE_DIR).mkdirs();
		invoices = InvoiceStorage.loadInvoices();
		System.out.println("[RMI] ucitano faktura: " + invoices.size());
	}
	

	@Override
	public void addInvoice(Invoice invoice) throws RemoteException {
		//invoices.add(invoice);
		//InvoiceStorage.saveInvoice(invoices);
		System.out.println("[RMI] Nova faktura primljena: " + invoice);
		
	}

	@Override
	public List<Invoice> getAllInvoices() throws RemoteException {
		return invoices;
	}
	
	 @Override
	    public void saveInvoice(Invoice invoice) throws RemoteException {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 
		try {
			 Gson gson = new GsonBuilder()
		                .setPrettyPrinting()
		                .registerTypeAdapter(LocalDate.class,
		                        (com.google.gson.JsonSerializer<LocalDate>)
		                                (src, typeOfSrc, context) ->
		                                        new com.google.gson.JsonPrimitive(src.toString()))
		                .registerTypeAdapter(LocalDateTime.class,
		                        (com.google.gson.JsonSerializer<LocalDateTime>)
		                                (src, typeOfSrc, context) ->
		                                        new com.google.gson.JsonPrimitive(src.toString()))
		                .create();
			 
			 String fileName = invoice.getSupplierName() + "_"
		                + invoice.getInvoiceId() + "_"
		                + invoice.getIssueDate().format(formatter) + ".json";

		        File file = new File(INVOICE_DIR, fileName);
		        
		        try (FileWriter writer = new FileWriter(file)) {
		            gson.toJson(invoice, writer);
		        }

		        System.out.println("[RMI Server] Invoice saved: " + file.getAbsolutePath());
		}catch (Exception e) {
			log.severe("Error while saving invoice");
	        e.printStackTrace();
	        throw new RemoteException("Failed to save invoice as JSON", e);
	    }
		 
}

}
