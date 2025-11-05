package net.etfbl.mdp.service.gui.rest;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestAppointment {
	private static final String BASE_URL = "http://localhost:8080/ServiceApp/api/appointments";
	
	public List<Appointment> getAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();
		try {
			URL url = new URL(BASE_URL + "/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if(conn.getResponseCode() != 200) {
				System.out.println("Error with GET method: " + conn.getResponseCode());
				return appointments;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = br.readLine()) != null) sb.append(line);
			JSONArray array = new JSONArray(sb.toString());
			for(int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Appointment a = new Appointment();
				a.setDate(obj.optString("date"));
				a.setDescription(obj.optString("description"));
				a.setId(obj.optString("id"));
				a.setOwnerUsername(obj.optString("ownerUsername"));
				a.setTime(obj.optString("time"));
				a.setStatus(obj.optString("status")); //TODO
				a.setType(obj.optString("type"));
				a.setComment(obj.optString("comment"));
				appointments.add(a);
			}
			br.close();
			conn.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return appointments;
	}

	public void approveAppointment(String id) {
		sendPutRequest(BASE_URL + "/approve/" + id);
	}
	
	public void rejectAppointment(String username) {
		sendPutRequest(BASE_URL + "/reject/" + username);
	}
	
	public void addComment(String id, String comment) {
	    try {
	        URL url = new URL(BASE_URL + "/comment/" + id);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(comment.getBytes("UTF-8"));
	        }

	        int responseCode = conn.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            System.out.println("✅ Comment added successfully for appointment " + id);
	        } else {
	            System.err.println("⚠️ Failed to add comment: " + responseCode);
	        }

	        conn.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	private void sendPutRequest(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.getResponseCode();
			conn.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
