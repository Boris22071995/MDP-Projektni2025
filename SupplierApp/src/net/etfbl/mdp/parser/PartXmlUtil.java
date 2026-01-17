package net.etfbl.mdp.parser;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.etfbl.mdp.model.Part;

public class PartXmlUtil {

	public static void savePartsToXml(String supplierName, List<Part> parts) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		Element root = doc.createElement("parts");
		doc.appendChild(root);

		for (Part p : parts) {
			Element pe = doc.createElement("part");

			Element code = doc.createElement("code");
			code.appendChild(doc.createTextNode(Part.encode(p.getCode())));
			pe.appendChild(code);

			Element name = doc.createElement("title");
			name.appendChild(doc.createTextNode(Part.encode(p.getTitle())));
			pe.appendChild(name);

			Element price = doc.createElement("price");
			price.appendChild(doc.createTextNode(String.valueOf(p.getPrice())));
			pe.appendChild(price);

			Element img = doc.createElement("image");
			img.appendChild(doc.createTextNode(Part.encode(p.getImageURL())));
			pe.appendChild(img);

			root.appendChild(pe);
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(doc), new StreamResult(new File(supplierName + "_parts.xml")));
	}

	public static List<Part> readPartsFromXml(String xmlFilePath) throws Exception {
		List<Part> parts = new ArrayList<>();
		File f = new File(xmlFilePath);
		if (!f.exists())
			return parts;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(f);

		NodeList nl = doc.getElementsByTagName("part");
		for (int i = 0; i < nl.getLength(); i++) {
			Element pe = (Element) nl.item(i);
			String code = Part.decode(pe.getElementsByTagName("code").item(0).getTextContent());

			String title = Part.decode(pe.getElementsByTagName("title").item(0).getTextContent());

			String priceStr = pe.getElementsByTagName("price").item(0).getTextContent();
			double price = 0.0;
			try {
				price = Double.parseDouble(priceStr);
			} catch (Exception ignored) {
			}
			String img = Part.decode(pe.getElementsByTagName("image").item(0).getTextContent());
			parts.add(new Part(code, title, price, img));
		}
		return parts;
	}

}
