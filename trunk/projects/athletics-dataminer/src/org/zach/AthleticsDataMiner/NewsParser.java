package org.zach.AthleticsDataMiner;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NewsParser {
	XmlWriter writer;

	NewsParser(String dataFile) {
		writer = new XmlWriter(dataFile);
	}

	public void startDataFile() {
		writer.startDataFile();
	}

	public void parseNews(String URL) {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		/*
		 * set it to Coalesce CDATA nodes because the descriptions are CDATA'd
		 */
		dbf.setCoalescing(true);
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(new URL(URL).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element rootElement = dom.getDocumentElement();
		NodeList itemNodeList = rootElement.getElementsByTagName("item");
		if (itemNodeList != null && itemNodeList.getLength() > 0) {
			for (int i = 0; i < itemNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) itemNodeList.item(i);
				NodeList attrNodeList = el.getChildNodes();
				String link = "";
				String title = "";
				String body = "";
				String date = "";
				for (int j = 0; j < attrNodeList.getLength(); j++) {
					Node attrEl = attrNodeList.item(j);
					if (attrEl.getNodeName().equalsIgnoreCase("link")) {
						link = attrEl.getTextContent().replace("\"", "'");
					} else if (attrEl.getNodeName().equalsIgnoreCase("title")) {
						title = removeHtmlFromString(attrEl.getTextContent()
								.replace("\"", "'"));
					} else if (attrEl.getNodeName().equalsIgnoreCase(
							"description")) {
						body = removeHtmlFromString(attrEl.getTextContent()
								.replace("\"", "'"));
					} else if (attrEl.getNodeName().equalsIgnoreCase("pubDate")) {
						date = attrEl.getTextContent().replace("\"", "'");
					}
				}
				System.out.println(title + "--" + body + "--" + link + "--"
						+ date);
				writer.writeNews(title, body, link, date);
			}
		}
	}

	public void endDataFile() {
		writer.endDataFile();
	}

	private String removeHtmlFromString(String input) {
		String output;
		// remove BR tags
		output = input.replaceAll("<BR>", "");

		// remove things in italics
		output = output.replaceAll("<[iI]>", "").replaceAll("</[iI]>", "");
		return output;
	}
}
