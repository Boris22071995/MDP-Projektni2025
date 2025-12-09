package net.etfbl.mdp.service.securechat;

import java.util.Arrays;

public class ServerEvents {
	
	public interface MessageListener{
		void onMessage(String from, String msg);
	}
	
	private static MessageListener listener;
	
	public static void setListener(MessageListener l) {			
		listener = l;
	}
	
	public static void messageReceived(String from, String msg) {
		if(listener != null) {
			listener.onMessage(from, msg);
			System.out.println("OPP-> "+msg);
		}
	}

}
