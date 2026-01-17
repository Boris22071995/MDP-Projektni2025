package net.etfbl.mdp.service.gui.rest;

import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.util.AppLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RestClient {

	private static final String BASE_URL = "http://localhost:8080/ServiceApp/api/clients";
	private static final Logger log = AppLogger.getLogger();
	public List<Client> getAllClients() {
		List<Client> clients = new ArrayList<>();
		try {
			URL url = new URL(BASE_URL + "/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				System.out.println("Error with GET method: " + conn.getResponseCode());
				return clients;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			JSONArray array = new JSONArray(sb.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Client c = new Client();
				c.setUsername(obj.optString("username"));
				c.setPassword(obj.optString("password"));
				c.setName(obj.optString("name"));
				c.setLastName(obj.optString("lastName"));
				c.setAddress(obj.optString("address"));
				c.setEmail(obj.optString("email"));
				c.setPhone(obj.optString("phone"));
				c.setVehicleData(obj.optString("vehicleData"));
				c.setApproved(obj.optBoolean("approved"));
				c.setBlocked(obj.optBoolean("blocked"));
				clients.add(c);
			}
			log.info("Clients are retrived.");
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			log.severe("Error while retriving clients.");
			e.printStackTrace();
		}
		return clients;
	}

	public void approveClient(String username) {
		sendPutRequest(BASE_URL + "/approve/" + username);
	}

	public void blockClient(String username) {
		sendPutRequest(BASE_URL + "/block/" + username);
	}

	public void unblockClient(String username) {
		sendPutRequest(BASE_URL + "/unblock/" + username);
	}

	public void deleteClient(String username) {
		try {
			URL url = new URL(BASE_URL + "/" + username);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.getResponseCode();
			conn.disconnect();
			log.info("Client is deleted");
		} catch (Exception e) {
			log.severe("Error while deleting client");
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
			log.info("Client is updated.");
		} catch (Exception e) {
			log.severe("Error while updating client");
			e.printStackTrace();
		}
	}

}
