package net.etfbl.mdp.service.gui.rest;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RestAppointment {
	private static final String BASE_URL = ConfigurationLoader.getString("appointments.url");
	private static final Logger log = AppLogger.getLogger();

	public List<Appointment> getAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();
		try {
			URL url = new URL(BASE_URL + "/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				log.warning("Error while retriving appointments");
				return appointments;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			JSONArray array = new JSONArray(sb.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Appointment a = new Appointment();
				a.setDate(obj.optString("date"));
				a.setDescription(obj.optString("description"));
				a.setId(obj.optString("id"));
				a.setOwnerUsername(obj.optString("ownerUsername"));
				a.setTime(obj.optString("time"));
				a.setStatus(obj.optString("status"));
				a.setType(obj.optString("type"));
				a.setComment(obj.optString("comment"));
				appointments.add(a);
			}
			log.info("All appointments are retrived.");
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			log.severe("Error while loading appointments");
			e.printStackTrace();
		}
		return appointments;
	}

	public List<Appointment> getAllAppointmentsByUsername(String username) {
		List<Appointment> appointments = new ArrayList<>();
		try {
			URL url = new URL(BASE_URL + "/user/"+username);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				log.warning("Error with GET method.");
				return appointments;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			JSONArray array = new JSONArray(sb.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Appointment a = new Appointment();
				a.setDate(obj.optString("date"));
				a.setDescription(obj.optString("description"));
				a.setId(obj.optString("id"));
				a.setOwnerUsername(obj.optString("ownerUsername"));
				a.setTime(obj.optString("time"));
				a.setStatus(obj.optString("status"));
				a.setType(obj.optString("type"));
				a.setComment(obj.optString("comment"));
				appointments.add(a);
			}
			log.info("Appointments by username are retrived.");
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			log.severe("Error while retriving appointments");
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
				log.info("Commented added successfully.");
			} else {
				log.warning("Failed to added comment.");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAppointments(List<Appointment> appointments) {
		for (Appointment a : appointments) {
			try {
				URL url = new URL(BASE_URL + "/" + a.getId());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("DELETE");
				conn.getResponseCode();
				conn.disconnect();
				log.info("Appointment are deleted.");
			} catch (Exception e) {
				log.severe("Error while deleting appointments");
				e.printStackTrace();
			}
		}
	}
	
	public void deleteAppointmetn(Appointment a) {
		try {
			URL url = new URL(BASE_URL + "/" + a.getId());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.getResponseCode();
			conn.disconnect();
			log.info("Appointment is deleted.");
		}catch(Exception e) {
			log.warning("Error while deleting appointment");
			e.printStackTrace();
		}
	}

}
