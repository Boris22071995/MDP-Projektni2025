package net.etfbl.mdp.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

private static Logger logger;
	
	static {
		logger = Logger.getLogger("MDPClientLogger");
		logger.setUseParentHandlers(false);		
		try {
			FileHandler fh = new FileHandler("supplier_app.log",true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		}catch(IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
}
