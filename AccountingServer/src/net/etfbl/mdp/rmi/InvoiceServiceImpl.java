package net.etfbl.mdp.rmi;

import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.util.InvoiceStorage;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {
	
	private List<Invoice> invoices;
	
	public InvoiceServiceImpl() throws RemoteException {
		invoices = InvoiceStorage.loadInvoices();
		System.out.println("[RMI] ucitano faktura: " + invoices.size());
	}
	

	@Override
	public void addInvoice(Invoice invoice) throws RemoteException {
		invoices.add(invoice);
		InvoiceStorage.saveInvoice(invoices);
		System.out.println("[RMI] Nova faktura primljena: " + invoice);
		
	}

	@Override
	public List<Invoice> getAllInvoices() throws RemoteException {
		return invoices;
	}
	
	 @Override
	    public void saveInvoice(String orderId, String client, double totalAmount) throws RemoteException {
		 
		 /*
		  * 
		  * 
		  *  String timestamp = java.time.LocalDateTime.now()
                .toString()
                .replace(":", "-"); // zamijeni ':' jer nije dozvoljen u imenu fajla

        // Naziv fajla: supplierName_timestamp.json
        String fileName = invoice.getSupplierName() + "_" + timestamp + ".json";

        // Jackson ObjectMapper za JSON serializaciju
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.findAndRegisterModules(); // da podrži LocalDateTime automatski

        // Snimi invoice u JSON fajl
        mapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File(fileName), invoice);

        System.out.println("[RMI Server] Invoice saved as JSON: " + fileName);
		  * */
		 
		 
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(orderId + ".ser"))) {
	            oos.writeObject("Invoice for " + client + " total: " + totalAmount);
	            System.out.println("[RMI Server] Invoice saved for " + client + totalAmount);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
