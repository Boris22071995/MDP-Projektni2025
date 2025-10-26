package net.etfbl.mdp.rmi;

import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.util.InvoiceStorage;

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
	

}
