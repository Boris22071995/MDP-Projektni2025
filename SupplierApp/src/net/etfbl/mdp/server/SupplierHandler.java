package net.etfbl.mdp.server;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.parser.PartXmlUtil;

import java.io.*;
import java.net.Socket;
import java.util.List;

import javax.net.ssl.SSLSocket;

public class SupplierHandler implements Runnable{
	
	    private SSLSocket socket;
	    private String supplierName;

	    public SupplierHandler(SSLSocket socket, String supplierName) {
	        this.socket = socket;
	        this.supplierName = supplierName;
	    }

	    @Override
	    public void run() {
	        try (
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
	            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)
	        ) {
	            String request = in.readLine();
	            if (request == null) return;

	            System.out.println("📩 Request received: " + request);

	            if (request.startsWith("GET_PARTS")) {
	            	
	            	
	                // Format: GET_PARTS|Supplier1
	                String[] parts = request.split("\\|");
	                String requestedSupplier = parts.length > 1 ? parts[1] : supplierName;

	                if (!requestedSupplier.equalsIgnoreCase(supplierName)) {
	                    out.println("ERROR|Wrong supplier requested!");
	                    out.println("END");
	                    return;
	                }

	                List<Part> partList = PartXmlUtil.readPartsFromXml(supplierName + "_parts.xml");
	                System.out.println(partList.size());
	                for (Part p : partList) {
	                	System.out.println(p);
	                    out.println(String.format("PART|%s|%s|%s|%s",
	                            Part.encode(p.getCode()),
	                            Part.encode(p.getTitle()),
	                            String.valueOf(p.getPrice()),
	                            Part.encode(p.getImageURL())));
	                }
	                out.println("END");
	                System.out.println("✅ Sent " + partList.size() + " parts to service.");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try { socket.close(); } catch (IOException ignored) {}
	        }
	    }
	
}
