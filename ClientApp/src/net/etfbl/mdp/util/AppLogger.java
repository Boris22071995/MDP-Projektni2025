package net.etfbl.mdp.util;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
	
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("MDPClientLogger");
		logger.setUseParentHandlers(false);		
		try {
			FileHandler fh = new FileHandler("client_app.log",true);
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
