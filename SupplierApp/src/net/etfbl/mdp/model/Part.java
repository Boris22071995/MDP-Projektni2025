package net.etfbl.mdp.model;

import java.io.Serializable;

public class Part implements Serializable {

	private String code;
	private String title;
	private double price;
	private String imageURL;
	
	public Part() {
		
	}

	public Part(String code, String title, double price, String imageURL) {
		super();
		this.code = code;
		this.title = title;
		this.price = price;
		this.imageURL = imageURL;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public String toString() {
		return "Part [code=" + code + ", title=" + title + ", price=" + price + "]";
	}
	
	
}
