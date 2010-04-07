package org.zach.AthleticsDataMiner;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GameParser {
	XmlWriter writer;

	GameParser(String dataFile) {
		writer = new XmlWriter(dataFile);
	}

	public void startDataFile() {
		writer.startDataFile();
	}

	public void endDataFile() {
		writer.endDataFile();
	}

	public void parseGames(String URL) {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
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
				String title = "";
				String date = "";
				String sport = "";
				for (int j = 0; j < attrNodeList.getLength(); j++) {
					Node attrEl = attrNodeList.item(j);
					if (attrEl.getNodeName().equalsIgnoreCase("title")) {
						title = attrEl.getTextContent().replace("\"", "'");
					} else if (attrEl.getNodeName().equalsIgnoreCase("pubDate")) {
						date = attrEl.getTextContent().replace("\"", "'");
					} else if (attrEl.getNodeName()
							.equalsIgnoreCase("category")) {
						if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Golf")) {
							sport = "10";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Men's Tennis")) {
							sport = "6";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Basketball")) {
							sport = "7";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Baseball")) {
							sport = "2";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Track")) {
							sport = "15";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Men's Golf")) {
							sport = "5";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Bowling")) {
							sport = "8";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Swimming")) {
							sport = "13";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Football")) {
							sport = "1";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Men's Basketball")) {
							sport = "3";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Men's Soccer")) {
							sport = "16";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Soccer")) {
							sport = "12";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Tennis")) {
							sport = "14";
						} else if (attrEl.getTextContent().equalsIgnoreCase(
								"Women's Lacrosse")) {
							sport = "11";
						}
					}
				}
				String game_id = Integer.toString((title.hashCode() + date
						.hashCode()));
				String awayteam = "";
				String hometeam = "";
				hometeam = getHomeTeam(title);
				if (hometeam.equalsIgnoreCase("error")) {
					hometeam = title;
					awayteam = "";
				} else {
					awayteam = getAwayTeam(title);
					if (awayteam.equalsIgnoreCase("error")) {
						hometeam = title;
						awayteam = "";
					}
				}

				System.out.println(hometeam + "--" + awayteam + "--" + sport
						+ "--" + date);
				writer.writeGame(hometeam, awayteam, sport, "regular", date,
						"", "");

			}
		}
	}

	private String getHomeTeam(String title) {
		String[] array = title.split("[A-Za-z0-9]* vs\\. ", 2);
		if (array.length == 2) {
			return "Vanderbilt";
		} else {
			array = title.split("[A-Za-z0-9]* at ", 2);
			if (array.length == 2) {
				return array[1].trim();
			} else {
				return "error";
			}
		}

	}

	private String getAwayTeam(String title) {
		String[] array = title.split("[A-Za-z0-9]* vs\\.", 2);
		if (array.length == 2) {
			return array[1].trim();
		} else {
			array = title.split("[A-Za-z0-9]* at ", 2);
			if (array.length == 2) {
				return "Vanderbilt";
			} else {
				return "error";
			}
		}
	}
}
