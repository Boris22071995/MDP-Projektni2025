package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.util.HttpUtil;
import org.json.JSONObject;

public class AuthService {

	private static final String BASE_URL = "http://localhost:8080/ServiceApp/api/clients";
	
	
	public Client login(String username, String password) {
		try {
			JSONObject json = new JSONObject();
			json.put("username", username);
			json.put("password", password);
			
			String response = HttpUtil.sendPost(BASE_URL + "/login", json.toString());
			JSONObject obj = new JSONObject(response);
			if(response.contains("Wrong password"))
				return null;
			 Client c = new Client();
	            c.setUsername(obj.getString("username"));
	            c.setName(obj.optString("name"));
	            c.setLastName(obj.optString("lastName"));
	            c.setVehicleData(obj.optString("vehicleData"));
	            c.setApproved(obj.optBoolean("approved"));
	            c.setBlocked(obj.optBoolean("blocked"));
	            return c;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	 public boolean register(Client client) {
	        try {
	            JSONObject json = new JSONObject();
	            json.put("username", client.getUsername());
	            json.put("password", client.getPassword());
	            json.put("name", client.getName());
	            json.put("lastName", client.getLastName());
	            json.put("vehicleData", client.getVehicleData());
	            json.put("address", client.getAddress());
	            json.put("phone", client.getPhone());
	            json.put("email", client.getEmail());
	            json.put("approved", client.isApproved());
	            json.put("blocked", client.isBlocked());
	            
	            

	            int response = HttpUtil.sendPostRegister(BASE_URL + "/register", json.toString());
	            return response >= 200 && response <= 300;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	
	
}
