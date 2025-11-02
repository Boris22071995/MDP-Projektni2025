package net.etfbl.mdp.rmi;

import net.etfbl.mdp.model.Invoice;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {
	
	private List<Invoice> invoices;
	
	public InvoiceServiceImpl() throws RemoteException {
		//invoices = InvoiceStorage.loadInvoices();
		//System.out.println("[RMI] ucitano faktura: " + invoices.size());
	}
	
//
//	@Override
//	public void addInvoice(Invoice invoice) throws RemoteException {
//		invoices.add(invoice);
//		//InvoiceStorage.saveInvoice(invoices);
//		System.out.println("[RMI] Nova faktura primljena: " + invoice);
//		
//	}
//
//	@Override
//	public List<Invoice> getAllInvoices() throws RemoteException {
//		return invoices;
//	}
	
	 @Override
	    public void saveInvoice(String orderId, String client, double totalAmount) throws RemoteException {
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(orderId + ".ser"))) {
	            oos.writeObject("Invoice for " + client + " total: " + totalAmount);
	            System.out.println("[RMI Server] Invoice saved for " + client);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
