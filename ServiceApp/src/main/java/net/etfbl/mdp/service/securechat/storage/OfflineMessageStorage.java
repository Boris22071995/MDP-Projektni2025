package net.etfbl.mdp.service.securechat.storage;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class OfflineMessageStorage {
	 private static final String FILE_NAME = "offlineMessages.xml";
	 
	 public static synchronized void saveMessage(String toUser, String fromUser, String message) {
	        try {
	            File file = new File(FILE_NAME);
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document doc;

	            if (file.exists()) {
	                doc = db.parse(file);
	            } else {
	                doc = db.newDocument();
	                Element root = doc.createElement("messages");
	                doc.appendChild(root);
	            }

	            Element root = doc.getDocumentElement();
	            Element msgElem = doc.createElement("message");

	            msgElem.setAttribute("to", toUser);
	            msgElem.setAttribute("from", fromUser);
	            msgElem.setTextContent(message);

	            root.appendChild(msgElem);

	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.transform(new DOMSource(doc), new StreamResult(file));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public static synchronized List<String> loadMessagesForUser(String username) {
	        List<String> messages = new ArrayList<>();
	        try {
	            File file = new File(FILE_NAME);
	            if (!file.exists()) return messages;

	            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = db.parse(file);
	            NodeList nodeList = doc.getElementsByTagName("message");

	            for (int i = 0; i < nodeList.getLength(); i++) {
	                Element msgElem = (Element) nodeList.item(i);
	                if (msgElem.getAttribute("to").equalsIgnoreCase(username)) {
	                    String from = msgElem.getAttribute("from");
	                    String text = msgElem.getTextContent();
	                    messages.add("[Od " + from + "]: " + text);
	                }
	            }

	            // Nakon učitavanja, brišemo te poruke iz XML-a
	            deleteMessagesForUser(username);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return messages;
	    }
	 
	 private static synchronized void deleteMessagesForUser(String username) {
	        try {
	            File file = new File(FILE_NAME);
	            if (!file.exists()) return;

	            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = db.parse(file);
	            NodeList nodeList = doc.getElementsByTagName("message");

	            List<Element> toRemove = new ArrayList<>();
	            for (int i = 0; i < nodeList.getLength(); i++) {
	                Element msgElem = (Element) nodeList.item(i);
	                if (msgElem.getAttribute("to").equalsIgnoreCase(username)) {
	                    toRemove.add(msgElem);
	                }
	            }

	            for (Element e : toRemove) {
	                e.getParentNode().removeChild(e);
	            }

	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.transform(new DOMSource(doc), new StreamResult(file));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
