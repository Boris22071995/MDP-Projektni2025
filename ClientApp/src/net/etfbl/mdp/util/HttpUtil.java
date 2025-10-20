package net.etfbl.mdp.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static String sendPost(String urlString, String jsonBody) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		
		try(OutputStream os = conn.getOutputStream()){
			os.write(jsonBody.getBytes());
		}
		
		return readResponse(conn);
	}
	
	public static String sendGet(String urlString) throws IOException{
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		return readResponse(conn);
	}
	
	
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader br;
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		else
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = br.readLine()) != null)
			sb.append(line);
		br.close();
		return sb.toString();
	}
	
}
