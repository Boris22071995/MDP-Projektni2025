package net.etfbl.mdp.model;

import java.io.Serializable;

public class Part implements Serializable {

	  private String id;
	    private String name;
	    private String manufacturer;
	    private double price;
	    private int quantity;
	    private String description;

	    public Part() {}

	    public Part(String id, String name, String manufacturer, double price, int quantity, String description) {
	        this.id = id;
	        this.name = name;
	        this.manufacturer = manufacturer;
	        this.price = price;
	        this.quantity = quantity;
	        this.description = description;
	    }
	    public Part(String id, String name, double price, String description) {
	    	this.id = id;
	    	this.name = name;
	    	this.price = price;
	    	this.description = description;
	    }
	   
	    public String getId() { return id; }
	    public void setId(String id) { this.id = id; }

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getManufacturer() { return manufacturer; }
	    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

	    public double getPrice() { return price; }
	    public void setPrice(double price) { this.price = price; }

	    public int getQuantity() { return quantity; }
	    public void setQuantity(int quantity) { this.quantity = quantity; }

	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }
	
}
