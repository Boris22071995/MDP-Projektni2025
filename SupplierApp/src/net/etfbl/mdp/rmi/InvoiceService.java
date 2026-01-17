package net.etfbl.mdp.rmi;

import net.etfbl.mdp.model.Invoice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InvoiceService extends Remote {

	void saveInvoice(Invoice invoice) throws RemoteException;

}
