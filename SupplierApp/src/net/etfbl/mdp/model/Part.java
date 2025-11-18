package net.etfbl.mdp.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
	
	public static String encode(String s) {
        try {
			return URLEncoder.encode(s == null ? "" : s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
    }
    public static String decode(String s) {
        try {
			return s == null ? "" : URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
    }

	@Override
	public String toString() {
		return "Part [code=" + code + ", title=" + title + ", price=" + price + "]";
	}
	
	
}
