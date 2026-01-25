package net.etfbl.mdp.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import net.etfbl.mdp.util.ConfigurationLoader;

public class OrderStatusStore {

	private static final ConcurrentHashMap<String, String> ordersMap =
            new ConcurrentHashMap<>();
	
	private static final String DIR = "configuration";
    private static final String FILE = DIR + File.separator + ConfigurationLoader.getString("file.name");

    private OrderStatusStore() {}

    public static void put(String orderId, String status) {
        ordersMap.put(orderId, status);
        appendToFile(orderId, status);
    }

    public static String get(String orderId) {
    	loadFromFile();
        return ordersMap.get(orderId);
    }
    
    private static void appendToFile(String orderId, String status) {
        try {
            Files.createDirectories(Paths.get(DIR));

            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FILE, true))) { 
                bw.write(orderId + "|" + status);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized void loadFromFile() {
        Path path = Paths.get(FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    ordersMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
