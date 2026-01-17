package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.rmi.InvoiceService;

import java.rmi.Naming;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class OrderQueueService {

	 private static Queue<Order> orderQueue = new LinkedList<>();

	    public static List<Order> getPendingOrders() {
	    	return orderQueue.stream().collect(Collectors.toList());
	    }

	    public static void addOrder(Order order) {
	        orderQueue.offer(order);
	    }
	    public static void processOrder(Invoice invoice, boolean approved) {
	    	Order order = orderQueue.stream().filter(o->o.getPartName().equals(invoice.getPartTitle())).findFirst().orElse(null);
	    	
	    	if(order==null)return;
	    	if(approved) {
	    		try {
	    			InvoiceService invoiceService = (InvoiceService)Naming.lookup("//localhost/InvoiceService");
	    			invoiceService.saveInvoice(invoice);
	    		}catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	orderQueue.remove(order);
	    	
	    }

	
}
