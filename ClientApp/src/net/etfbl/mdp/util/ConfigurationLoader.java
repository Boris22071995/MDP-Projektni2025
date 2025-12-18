package net.etfbl.mdp.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationLoader {
	private static final Logger log = AppLogger.getLogger();
	private static Properties properties = new Properties();
	
	static {
		try(InputStream is = new FileInputStream("configuration/config.properties")){
			properties.load(is);
		}catch(Exception e) {
			log.severe("Cannot load configuration properties");
			throw new RuntimeException("Cannot load configuration properties", e);
		}
	}
	
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	public static int getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}

}
