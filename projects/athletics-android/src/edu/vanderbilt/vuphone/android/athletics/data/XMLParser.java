package edu.vanderbilt.vuphone.android.athletics.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLParser {
	// initialize my data
	List myData; // this will eventually be a list of my data objects (I realize this is generic
			     // - this makes it more flexible - just be careful with code)
	Document dom; // this will be the raw data after the XML file is parsed
	
	public XMLParser() {
		myData = new ArrayList (); // ignore the generic warning
	}
	
	private void parseXmlFile(String type){
		
		// creating the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Create a document builder using the factory
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using the document builder to get DOM representation of the XML file
			dom = db.parse(type + ".xml");


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	private void parseDocument(String type){
		//get the root element
		Element docEle = dom.getDocumentElement();

		//get a nodelist of  elements
		NodeList nl = docEle.getElementsByTagName("dataobject");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the dataobject element
				Element el = (Element)nl.item(i);

				//get the DataObject object
				if (type.equalsIgnoreCase("event")) {
					EventDataObject e = getEventDataObject(el);
					//add it to list
					myData.add(e); // ignore the warning
				} else if (type.equalsIgnoreCase("roster")) {
					RosterDataObject e = getRosterDataObject(el);
					// add it to list
					myData.add(e); // ignore the warning
				} else {
					// TODO: write an error handler for this situation
				}
				

				
			}
		}
	}
	private EventDataObject getEventDataObject(Element empEl) {

		// for each <EventDataObject> element get string values of
		// type, title, date, time, description, sport, ourscore, theirscore
		String type = getStringValue(empEl,"type");
		String title = getStringValue(empEl,"title");
		int date = getIntValue(empEl,"date");
		int time = getIntValue(empEl,"time");
		String description = getStringValue(empEl,"description");
		String sport = getStringValue(empEl,"sport");
		int ourscore = getIntValue(empEl,"ourscore");
		int theirscore = getIntValue(empEl,"theirscore");
		
		// Create a new DataObject with the processed values
		EventDataObject e = new EventDataObject(type, title, date, time, description, sport, ourscore, theirscore);

		return e;
	}

	   private RosterDataObject getRosterDataObject(Element empEl) {
		   
		String sport = getStringValue(empEl,"sport");
		List<List<String>> playerdata = getPlayerData(empEl,"player");
		
		// Create a new DataObject with the processed values
		RosterDataObject e = new RosterDataObject(sport, playerdata);

		return e;
	}
	   private List<List<String>> getPlayerData(Element ele, String tagName) { // this gets stats by ORDER OF ELEMENTS IN XML FILE
		   List<List<String>> playerdata = new ArrayList<List<String>>();
			try {
			NodeList nl = ele.getElementsByTagName(tagName); // node list will be each player (1st dimension of array)
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {
					Element el = (Element)nl.item(i);
					List<String> emptylist = new ArrayList(); // this could screw up later TODO: FIX IT
					playerdata.add(emptylist);
					NodeList nl2 = el.getElementsByTagName("stat"); // this is the node list for stats (2nd dimension of array)
					for(int j=0; j < nl2.getLength();j++) {
						Element el3 = (Element)nl2.item(j);
						playerdata.get(i).add((String)el3.getFirstChild().getNodeValue());
					}
					
				}
				
			}

			return playerdata;
			
			// this catches null fields which otherwise would cause a NullPointerException
			} catch(NullPointerException e) { 
				return playerdata; // this is currently a kludge to get it to compile TODO: FIX IT
			}
		}
	private String getStringValue(Element ele, String tagName) {
		try {
		String textVal = "";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
		
		// this catches null fields which otherwise would cause a NullPointerException
		} catch(NullPointerException e) { 
			return ""; // return a blank field (another kludge) TODO: fix it
		}
	}
	private int getIntValue(Element ele, String tagName) {
		try {
		int intVal = -1; // we will assign a default value of negative one
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			intVal = Integer.parseInt(el.getFirstChild().getNodeValue());
		}

		return intVal;
		// this catches blank fields which otherwise would cause a NullPointerException
		} catch(NullPointerException e) { 
			return -1;
		}
	}
	public List sendDataObject(String type) { // ignore this warning
		parseXmlFile(type);
		parseDocument(type);
		return myData;
	}
}
