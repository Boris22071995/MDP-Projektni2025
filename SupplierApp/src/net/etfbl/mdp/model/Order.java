package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderId;
    private String partName;
    private int quantity;
    private double price;
    private String clientUsername;
    private LocalDateTime createdAt;
    private boolean  status;
    
    public Order() {
    	
    }

	public Order(String orderId, String partName, int quantity, double price, String clientUsername) {
		super();
		this.orderId = orderId;
		this.partName = partName;
		this.quantity = quantity;
		this.price = price;
		this.clientUsername = clientUsername;
		this.createdAt = LocalDateTime.now();
        this.status = false;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", partName=" + partName + ", quantity=" + quantity + ", price=" + price
				+ ", clientUsername=" + clientUsername + ", createdAt=" + createdAt + ", status=" + status + "]";
	}
    
    
}
