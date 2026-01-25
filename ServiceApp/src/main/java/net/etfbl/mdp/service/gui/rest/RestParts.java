package net.etfbl.mdp.service.gui.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

public class RestParts {
	
	private static final String BASE_URL = ConfigurationLoader.getString("parts.url");
	private static final Logger log = AppLogger.getLogger();
	
	public List<Part> getAllParts() {
		List<Part> parts = new ArrayList<>();
		
		try {
			URL url = new URL(BASE_URL + "/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if(conn.getResponseCode() != 200) {
				log.warning("Error while retriving parts from database");
				return parts;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = br.readLine()) != null)
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
		}catch(Exception e) {
			log.severe("Error while retriving parts");
			e.printStackTrace();
		}
		return parts;
				
	}

	public void addPart(Part p) {
		try {
			System.out.println("Da l udje u REST");
			
			URL url = new URL(BASE_URL + "/create");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			JSONObject json = new JSONObject();
			json.put("description", p.getDescription());
			json.put("id", p.getId());
			json.put("manufacturer", p.getManufacturer());
			json.put("name", p.getName());
			json.put("price", p.getPrice());
			json.put("quantity", p.getQuantity());
			
			try(OutputStream os = conn.getOutputStream()){
				os.write(json.toString().getBytes(StandardCharsets.UTF_8));
			}
			int responseCode = conn.getResponseCode(); 
			System.out.println("POST Response code: " + responseCode);
		}catch(Exception e) {
			log.severe("Error while adding part to database");
			e.printStackTrace();
		}
		
	}

	public void updatePart(Part p) {
		try {
			System.out.println("Da l smo u restu?");
			URL url = new URL(BASE_URL + "/update");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			JSONObject json = new JSONObject();
			json.put("description", p.getDescription());
			json.put("id", p.getId());
			json.put("manufacturer", p.getManufacturer());
			json.put("name", p.getName());
			json.put("price", p.getPrice());
			json.put("quantity", p.getQuantity());
			
			try(OutputStream os = conn.getOutputStream()){
				os.write(json.toString().getBytes(StandardCharsets.UTF_8));
			}
			int responseCode = conn.getResponseCode(); 
			System.out.println("POST Response code: " + responseCode);
		}catch(Exception e) {
			log.severe("Error while adding part to database");
			e.printStackTrace();
		}
		
	}

	public void deletePart(String id) {
		try {
			URL url = new URL(BASE_URL + "/" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.getResponseCode();
			conn.disconnect();
			log.info("Part is deleted.");
			int responseCode = conn.getResponseCode(); 
			System.out.println("POST Response code: " + responseCode);
		}catch(Exception e) {
			log.warning("Error while deleting parts");
			e.printStackTrace();
		}
		
	}

}
