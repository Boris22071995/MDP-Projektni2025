package net.etfbl.mdp.service.securechat;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class SSLClientHandler implements Runnable {

	 private SSLSocket socket;
	    private BufferedReader in;
	    private PrintWriter out;
	    private String name;

	    public SSLClientHandler(SSLSocket socket) {
	        this.socket = socket;
	    }

	    public String getName() {
	        return name;
	    }

	    @Override
	    public void run() {
	        try {
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out = new PrintWriter(socket.getOutputStream(), true);

	            out.println("Unesite korisničko ime: ");
	            name = in.readLine();
	            SSLChatServer.broadcast("🔒 " + name + " se priključio sigurnom chatu.", this);

	            String message;
	            while ((message = in.readLine()) != null) {
	                if ("exit".equalsIgnoreCase(message)) break;
	               //bilo je samo ovo a dodajem cijeli kod ispod SSLChatServer.broadcast(name + ": " + message, this); i metodu u server sendprivate
	                if (message.startsWith("@")) {
	                    int sep = message.indexOf(":");
	                    if (sep > 1) {
	                        String toUser = message.substring(1, sep).trim();
	                        String content = message.substring(sep + 1).trim();
	                        SSLChatServer.sendPrivate(toUser, "[Privatno od " + name + "]: " + content);
	                    }
	                } else {
	                    SSLChatServer.broadcast(name + ": " + message, this);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            SSLChatServer.removeClient(this);
	            SSLChatServer.broadcast("❌ " + name + " je napustio chat.", this);
	            try {
	                socket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public void sendMessage(String message) {
	        out.println(message);
	    }
	
}
