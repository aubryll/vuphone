package edu.vanderbilt.vuphone.android.athletics.data.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.vanderbilt.vuphone.android.athletics.data.objects.*;

public class SportXmlParser {
	public ArrayList<Game> games;
	public ArrayList<Team> teams;
	
	Document dom; // this will be the raw data after the XML file is parsed
	
	// build constructor to create an instance of our three ArrayLists
	public SportXmlParser(String type) {
		games = new ArrayList<Game>();
		teams = new ArrayList<Team>();
		
		// check for the existence of the version file for the sport we are attempting to get data for
		try {
			@SuppressWarnings("unused") // TODO: find a better way to check if this exists
			FileInputStream FileReader = new FileInputStream(type + ".version");
		} catch(FileNotFoundException e) {
			try {
				FileOutputStream versionOutput = new FileOutputStream(type + ".version");
				PrintStream versionPrinter = new PrintStream(versionOutput);
				versionPrinter.println("0");
				versionPrinter.close();
			} catch(Exception f) {
				f.printStackTrace();
			}
		}
		
		// check version on device against version on the internet and download new file if needed
		try {
			URL Version = new URL("http://people.vanderbilt.edu/~zach.mccormick/" + type + ".version");
			URLConnection VersionConnection = Version.openConnection();
			FileInputStream FileReader = new FileInputStream(type + ".version");
			BufferedReader VersionOnline = new BufferedReader(new InputStreamReader(VersionConnection.getInputStream()));
			BufferedReader VersionOffline = new BufferedReader(new InputStreamReader(FileReader));
			String onlineVersion = VersionOnline.readLine();
			String offlineVersion = VersionOffline.readLine();
			if (onlineVersion.equalsIgnoreCase(offlineVersion) == false ) {
				// wrong version
				// download the data from the server
				try {
					URL XmlData = new URL("http://people.vanderbilt.edu/~zach.mccormick/" + type + ".xml");
					URLConnection DataConnection = XmlData.openConnection();
					BufferedReader XmlOnline = new BufferedReader(new InputStreamReader(DataConnection.getInputStream()));
					// write the xml file
					FileOutputStream output = new FileOutputStream(type + ".xml");
					PrintStream printer = new PrintStream(output);
					while (true) {
						String NextLine = new String(XmlOnline.readLine());
						if(NextLine.equalsIgnoreCase("</" + type + ">")) {
							printer.println(NextLine);
							printer.close();
							break;
						} else {
							printer.println(NextLine);
						}
						
					}
					// write the new version file
					FileOutputStream versionOutput = new FileOutputStream(type + ".version");
					PrintStream versionPrinter = new PrintStream(versionOutput);
					versionPrinter.println(onlineVersion);
					versionPrinter.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		parseXmlFile(type);
		parseDocument();
	}
	
	private void parseXmlFile(String type){
		// create a DocumentBuilderFactory
		// documentation: "defines a factory API that enables applications to obtain a parser
		// that produces DOM object trees from XML elements"
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// try to create a parser and create our Document "dom" using the results of the parser
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(type + ".xml");
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace(); // catches parser configuration exceptions
		}catch(SAXException se) {
			se.printStackTrace(); // catches SAX parser exceptions (bad XML)
		}catch(IOException ioe) {
			ioe.printStackTrace(); // catches IO exceptions (file doesn't exist, etc)
		}
	}
	private void parseDocument(){
		
		// get the root element, which will be the sport in our case
		Element rootElement = dom.getDocumentElement();
		
		// get a nodelist of games
		NodeList gameNodeList = rootElement.getElementsByTagName("game");
		
		// build each game and add it and its properties to the list of games
		if(gameNodeList != null && gameNodeList.getLength() > 0) {
			for(int i = 0 ; i < gameNodeList.getLength();i++) {
				// get the i(th) game in the list 
				Element el = (Element)gameNodeList.item(i);
				
				// create a specific game by sending the element to the getGame function
				// and add that specific game to the list of games
				Game specificGame = getGame(el);
				games.add(specificGame);
			}
		}
		
		// get a nodelist of teams
		NodeList teamNodeList = rootElement.getElementsByTagName("team");
		
		// build each team and add it and its properties to the list of teams
		if(teamNodeList != null && teamNodeList.getLength() > 0) {
			for(int i = 0 ; i < teamNodeList.getLength();i++) {
				// get the i(th) team in the list 
				Element el = (Element)teamNodeList.item(i);
				
				// build a nodelist for players
				NodeList playerNodeList = el.getElementsByTagName("player");
				
				// build an ArrayList of players
				ArrayList<Player> teamPlayers = new ArrayList<Player>();
				
				// build each player and add it to the ArrayList for the team
				if(playerNodeList != null && playerNodeList.getLength() > 0) {
					for(int j = 0; j < playerNodeList.getLength(); j++) {
						// get the i(th) player in the team
						Element e = (Element)playerNodeList.item(j);
						
						// create a specific player by sending the element to the getPlayer function
						// and add that specific player to the list of players
						Player specificPlayer = getPlayer(e);
						teamPlayers.add(specificPlayer);
						
					}
				}
				
				// get all values of this particular team
				String sport = el.getAttribute("sport");
				String school = el.getAttribute("school");
				String name = el.getAttribute("name");
				String conference = el.getAttribute("conference");
				int wins = Integer.valueOf(el.getAttribute("wins"));
				int losses = Integer.valueOf(el.getAttribute("losses"));
				int conferenceWins = Integer.valueOf(el.getAttribute("conferenceWins"));
				int conferenceLosses = Integer.valueOf(el.getAttribute("conferenceLosses"));
				
				// create a specific team out of the values we have
				// and add that specific team to the list of teams
				Team specificTeam = new Team(sport, school, name, conference, wins, losses, conferenceWins, conferenceLosses, teamPlayers);
				teams.add(specificTeam);
			}
		}
		
	}
	
	private Game getGame(Element el) {
		// for each game, we will get the following attributes
		String sport = el.getAttribute("sport");
		String gender = el.getAttribute("gender");
		String type = el.getAttribute("type");
		String opponent = el.getAttribute("opponent");
		String place = el.getAttribute("place");
		String description = el.getAttribute("description");
		
		// to use the Date object, we must parse the String input
		ParsePosition pos = new ParsePosition(0); // start at the first character
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm z"); // "01-23-2010 18:30 CST" as an example
		Date time = sdf.parse(el.getAttribute("time"), pos); // set time
		
		boolean played = Boolean.valueOf(el.getAttribute("played"));
		int ourScore = 0;
		int theirScore = 0;
		if(played) {
			ourScore = Integer.valueOf(el.getAttribute("ourScore"));
			theirScore = Integer.valueOf(el.getAttribute("theirScore"));
		} 
		
		// create a new Game using the values just retrieved and return it
		if (played == true) {
			Game x = new Game(sport, gender, type, opponent, place, time, description, ourScore, theirScore);
			return x;
		} else {
			Game x = new Game(sport, gender, type, opponent, place, time, description);
			return x;
		}

	}
	

	private Player getPlayer(Element el) {
		String name = el.getAttribute("name");
		String position = el.getAttribute("position");
		String year = el.getAttribute("position");
		Map<String, String> stats = new HashMap<String, String>();
		
		// now we have a variable number of child nodes for stats
		// so we will use another nodelist and add each child node to our stats
		NodeList statNodeList = el.getElementsByTagName("stat");
		if(statNodeList !=null && statNodeList.getLength() > 0) {
			for(int i = 0; i < statNodeList.getLength(); i++) {
				Element specificStat = (Element)statNodeList.item(i);
					stats.put(specificStat.getAttribute("type"), specificStat.getFirstChild().getNodeValue());
			}
		}
		
		// now that we have collected all of the data, create a new specific player
		// and return it
		Player specificPlayer = new Player(name, position, year, stats);
		return specificPlayer;
	}

}
