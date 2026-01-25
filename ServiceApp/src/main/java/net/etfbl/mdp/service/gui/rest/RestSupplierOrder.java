package net.etfbl.mdp.service.gui.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class RestSupplierOrder {

	private String BASE_URL = ConfigurationLoader.getString("supplier.url");
	private static final Logger log = AppLogger.getLogger();
	
	public List<Part> getAllParts(String supplier) {
		List<Part> parts = new ArrayList<>();
		try {
			URL url = new URL(BASE_URL + "/all/" + supplier);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				System.out.println("Error with GET method: " + conn.getResponseCode());
				return parts;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			JSONArray array = new JSONArray(sb.toString());
			for(int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Part p = new Part();
				p.setDescription(obj.optString("description"));
				p.setId(obj.optString("id"));
				p.setManufacturer(obj.optString("manufacturer"));
				p.setName(obj.optString("name"));
				p.setPrice(obj.optDouble("price"));
				p.setQuantity(obj.optInt("quantity"));
				parts.add(p);
			}
			log.info("All parts are retrived");
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			log.severe("Error while retriving parts");
			e.printStackTrace();
		}
		return parts;
	}
}
