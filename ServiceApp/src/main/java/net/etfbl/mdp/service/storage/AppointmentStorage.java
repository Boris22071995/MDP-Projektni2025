package net.etfbl.mdp.service.storage;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.util.AppLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentStorage {
	private static final String FILE = "appointments.json";
	private static final Logger log = AppLogger.getLogger();

	public static synchronized List<Appointment> loadAll() {
		List<Appointment> list = new ArrayList<>();
		File f = new File(FILE);
		if (!f.exists())
			return list;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			if (sb.length() == 0)
				return list;
			JSONArray arr = new JSONArray(sb.toString());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				Appointment a = new Appointment();
				a.setId(o.optString("id"));
				a.setOwnerUsername(o.optString("ownerUsername"));
				a.setDate(o.optString("date"));
				a.setTime(o.optString("time"));
				a.setType(o.optString("type"));
				a.setDescription(o.optString("description"));
				a.setStatus(o.optString("status"));
				list.add(a);
			}
		} catch (Exception e) {
			log.severe("Error while loading appointments.");
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized void saveAll(List<Appointment> list) {
		JSONArray arr = new JSONArray();
		for (Appointment a : list) {
			JSONObject o = new JSONObject();
			o.put("id", a.getId());
			o.put("ownerUsername", a.getOwnerUsername());
			o.put("date", a.getDate());
			o.put("time", a.getTime());
			o.put("type", a.getType());
			o.put("description", a.getDescription());
			o.put("status", a.getStatus());
			arr.put(o);
		}
		try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
			pw.write(arr.toString(2));
		} catch (IOException e) {
			log.severe("Error while saving appointments.");
			e.printStackTrace();
		}
	}

}
