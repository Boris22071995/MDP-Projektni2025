package net.etfbl.mdp.service;


import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.util.ConfigurationLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
	
	private static final String BASE_URL = ConfigurationLoader.getString("appointments.url");
	
	 public List<Appointment> getAppointmentsByUser(String username) {
	        List<Appointment> list = new ArrayList<>();
	        try {
	            URL url = new URL(BASE_URL + "/user/" + username);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "application/json");

	            if (conn.getResponseCode() != 200) {
	                return list;
	            }

	            String jsonText = readResponse(conn.getInputStream());
	            JSONArray array = new JSONArray(jsonText);

	            for (int i = 0; i < array.length(); i++) {
	                JSONObject o = array.getJSONObject(i);
	                Appointment a = new Appointment();
	    
	                a.setId(o.optString("id"));
	                a.setOwnerUsername(o.optString("ownerUsername"));
	                a.setDate(o.optString("date"));
	                a.setTime(o.optString("time"));
                    a.setType(o.optString("type"));
	                a.setDescription(o.optString("description"));
	                a.setStatus(o.optString("status"));
	                a.setComment(o.optString("comment"));
	                list.add(a);
	            }
	            conn.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return list;
	    }

	 
	   public boolean createAppointment(Appointment a) {
	        try {
	            URL url = new URL(BASE_URL + "/create");
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.setDoOutput(true);

	            JSONObject json = new JSONObject();
	            json.put("id",a.getId());
	            json.put("ownerUsername", a.getOwnerUsername());
	            json.put("date", a.getDate());
	            json.put("time", a.getTime());
	            json.put("type", a.getType());
	            json.put("description", a.getDescription());
	            json.put("status", a.getStatus());
	            json.put("comment", " ");

	            try (OutputStream os = conn.getOutputStream()) {
	                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
	            }

	            int code = conn.getResponseCode();
	            conn.disconnect();
	            return code == 201 || code == 200;

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	   
	   private static String readResponse(InputStream is) throws IOException {
	        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null)
	            sb.append(line.trim());
	        br.close();
	        return sb.toString();
	    }

}
