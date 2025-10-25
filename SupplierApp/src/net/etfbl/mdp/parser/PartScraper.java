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
	
	public static List<Part> fetchParts() throws IOException {
		String url = "https://www.pkwteile.de/autoteile/bmw-ersatzteile/3-e90/136829/10130/bremsbelag";
		//Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").get();
		
		   Document doc = Jsoup.connect(url)
                   .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.6613.84 Safari/537.36")
                   .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                   .header("Accept-Language", "en-US,en;q=0.9")
                   .header("Accept-Encoding", "gzip, deflate, br")
                   .referrer("https://www.google.com/")
                   .ignoreHttpErrors(true)
                   .timeout(20000)
                   .get();
		
		List<Part> parts = new ArrayList<>();
		Elements products = doc.select(".article--product");
		
		for(Element product : products) {
			String code = product.attr("data-article-number");
			String title = product.select(".product--title").text();
			String priceText = product.select(".price--default").text().replace("€", "").replace(",", ".").trim();
            double price = 0.0;
            try {
            	price = Double.parseDouble(priceText);
            }catch(Exception ignored) {
            	
            }
            String image = product.select(".image--media img").attr("data-src");
            
            parts.add(new Part(code, title, price, image));
		}
		return parts;
	}

}
