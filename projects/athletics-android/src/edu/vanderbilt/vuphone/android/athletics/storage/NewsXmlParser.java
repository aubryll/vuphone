package edu.vanderbilt.vuphone.android.athletics.storage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * static helper method http://vucommodores.cstv.com/rss/rss-index.html
 * 
 * use these xml files as links
 * 
 * @author Zach
 * 
 */
public class NewsXmlParser {

	public static ArrayList<HashMap<String, String>> parseNews(String URL) {
		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
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
						link = attrEl.getFirstChild().getNodeValue().replace(
								"\"", "'");
					} else if (attrEl.getNodeName().equalsIgnoreCase("title")) {
						title = attrEl.getFirstChild().getNodeValue();
					} else if (attrEl.getNodeName().equalsIgnoreCase(
							"description")) {
						body = attrEl.getFirstChild().getNodeValue();
					} else if (attrEl.getNodeName().equalsIgnoreCase("pubDate")) {
						date = attrEl.getFirstChild().getNodeValue().replace(
								"\"", "'");
					}
				}
				HashMap<String, String> current = new HashMap<String, String>();
				current.put("title", title);
				current.put("body", body);
				current.put("link", link);
				current.put("date", date);
				results.add(current);
			}
		}
		return results;
	}
}
