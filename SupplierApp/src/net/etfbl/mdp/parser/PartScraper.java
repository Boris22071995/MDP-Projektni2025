package net.etfbl.mdp.parser;

import net.etfbl.mdp.model.Part;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartScraper {
	
	public static List<Part> fetchParts(String url) throws IOException {

		//String url = "https://www.autohub.ba/ts/senzor-temperature-rashladne-tecnosti-2/bmw-3-e21-3206-90kw?q=";	
		//String url = "https://www.autohub.ba/ts/nosac-motora-2/bmw-3-e21-3206-90kw?q=";
		//String url = "https://www.autohub.ba/ts/filter-vazduha-2/bmw-3-e21-3206-90kw?q=";
		//String url = "https://www.autohub.ba/ts/pumpa-za-vodu-2/bmw-3-e21-3206-90kw?q=";
		Document doc = Jsoup.connect(url)
		        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.6613.84 Safari/537.36")
		        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		        .header("Accept-Language", "en-US,en;q=0.9")
		        .referrer("https://www.google.com/")
		        .ignoreHttpErrors(true)
		        .timeout(20000)
		        .get();
		         
	        List<Part> parts = new ArrayList<>();
	        Elements products = doc.select("div.products-container");
	        for(Element product : products.select("div.item-box")) {
	        	String productName = product.select("h2.product-title a").text();
	        	String[] splitName = productName.split(" ");
	        	String code = "";
	        	String title = "";
	        	code = splitName[splitName.length - 1];
	        	for(int i = 0; i < splitName.length - 2; i ++) {
	        		title+=splitName[i];	
	        		
	        	}
	        	
	        	String productPrice = product.select("span.price-value").text();
	        	
	        	double price = 0.0;
	        	try {
	        		if("Cijena na poziv".equals(productPrice)) {
	        			//TODO math random
		        		price = 25.00;
		        	}
	        		else if(productPrice.contains(" ")) {
	        			String[] prices = productPrice.split(" ");
	        			String tmpPrice = prices[0].replace(",",".").trim();
	        			price = Double.parseDouble(tmpPrice);
	        		}
	        		else {
		        		productPrice = productPrice.replace(",", ".").trim();
		        		price = Double.parseDouble(productPrice);
		        	}
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        	
	        	String image = product.select("img").attr("src");
	        	
	        	
	        	parts.add(new Part(code, title, price, image));
	        	
	        }
	      
	        return parts;
	}

}
