package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {

	 private static final long serialVersionUID = 1L;

	    private String orderId;
	    private String clientUsername;
	    private String partName;
	    private int quantity;
	    private double price;
	    private LocalDateTime time;

	    public Order() {
	    	this.time = LocalDateTime.now();
	    }

	    public Order(String orderId, String clientUsername, String partName, int quantity, double price) {
	        this.orderId = orderId;
	        this.clientUsername = clientUsername;
	        this.partName = partName;
	        this.quantity = quantity;
	        this.price = price;
	        this.time = LocalDateTime.now();
	    }

	    public String getOrderId() { return orderId; }
	    public String getClientUsername() { return clientUsername; }
	    public String getPartName() { return partName; }
	    public int getQuantity() { return quantity; }
	    public double getPrice() { return price; }
	    public LocalDateTime getTime() { return time; }

	    @Override
	    public String toString() {
	        return String.format("[%s] %s x%d (%.2f€)", clientUsername, partName, quantity, price);
	    }
    
}
