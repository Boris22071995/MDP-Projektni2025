package net.etfbl.mdp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.etfbl.mdp.model.Client;

public class ClientService {
	private static final String OSNOVNI_URL = "http://localhost:8080/ServiceApp/api/clients";
	
	public static ArrayList<Client> getClients() {
		ArrayList<Client> clients = new ArrayList<>();
		try {
			URL url = new URL(OSNOVNI_URL + "/all");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			JSONArray result = new JSONArray();
			while ((output = br.readLine()) != null) {
				try {
					result = new JSONArray(output);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				for (int i = 0; i < result.length(); i++) {
					JSONObject jsonClient = result.getJSONObject(i);
					System.out.println(jsonClient.toString());
					clients.add(new Client(jsonClient.getString("password"), jsonClient.getString("username"),jsonClient.getString("username"),
							jsonClient.getString("username"),jsonClient.getString("username"),jsonClient.getString("username"),jsonClient.getString("username"),
							jsonClient.getString("username"),jsonClient.getBoolean("approved"),jsonClient.getBoolean("blocked")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clients;
	}
	
}
