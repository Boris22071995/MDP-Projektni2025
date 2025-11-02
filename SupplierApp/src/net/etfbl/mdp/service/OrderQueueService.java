package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Order;
import net.etfbl.mdp.rmi.InvoiceService;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class OrderQueueService {

	 private static Queue<Order> orderQueue = new LinkedList<>();

	    public static List<Order> getPendingOrders() {
	    	//System.out.println("OVO ME ZANIMA OVDJE DA L SE UDJE");
	    	return orderQueue.stream().collect(Collectors.toList());
	    }

	    public static void addOrder(Order order) {
	        orderQueue.offer(order);
	    }

	    public static void processOrder(String orderId, boolean approved) {
	        Order order = orderQueue.stream()
	                .filter(o -> o.getOrderId().equals(orderId))
	                .findFirst()
	                .orElse(null);

	        if (order == null) return;

	        if (approved) {
	            order.setStatus(approved);
	            double total = order.getPrice() * 1.17; // PDV 17%
	            try {
	                InvoiceService invoiceService =
	                        (InvoiceService) Naming.lookup("//localhost/InvoiceService");
	                invoiceService.saveInvoice(order.getOrderId(), order.getClientUsername(), total);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        orderQueue.remove(order);
	    }
	
}
