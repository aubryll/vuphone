package org.zach.AthleticsDataMiner;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TeamParser {
	XmlWriter writer;

	TeamParser(String dataFile) {
		writer = new XmlWriter(dataFile);
	}

	public void startDataFile() {
		writer.startDataFile();
	}

	public void parseTeams(String URL) {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(new FileInputStream(new File(URL)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element rootElement = dom.getDocumentElement();
		NodeList itemNodeList = rootElement.getElementsByTagName("team");
		if (itemNodeList != null && itemNodeList.getLength() > 0) {
			for (int i = 0; i < itemNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) itemNodeList.item(i);
				String school = "";
				String name = "";
				String location = "";
				String conference = "";
				int rank = 0;
				school = el.getAttribute("school");
				name = el.getAttribute("name");
				location = el.getAttribute("location");
				conference = el.getAttribute("conference");
				rank = getRank(school);
				System.out.println(school + "--" + name + "--" + location
						+ "--" + conference + "--" + rank);
				writer.writeTeam(school, name, location, conference, rank);
			}
		}
	}

	public void endDataFile() {
		writer.endDataFile();
	}

	private int getRank(String school) {
		// TODO: INSERT RANKING PARSING CODE HERE
		return 0; // for debugging
	}
}
