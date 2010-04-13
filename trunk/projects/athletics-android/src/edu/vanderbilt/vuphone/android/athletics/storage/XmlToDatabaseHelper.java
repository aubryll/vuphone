package edu.vanderbilt.vuphone.android.athletics.storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import edu.vanderbilt.vuphone.android.athletics.R;

/**
 * Multifunction XML and Database reading/building class. On construction, the
 * constructor checks for the existance of the version file and creates it if it
 * is missing. Both isUpdated() and updateDatabase() need network permission to
 * function.
 * 
 * @author Zach McCormick
 * 
 */
public class XmlToDatabaseHelper {
	private static String versionFilename = "data.version";
	private static String versionURL = "http://people.vanderbilt.edu/~zach.mccormick/data.version";
	private static String dataURL = "http://people.vanderbilt.edu/~zach.mccormick/data.xml";
	private static String appVersion = "0.1";
	Document dom; // this will be the raw data after the XML file is parsed
	DatabaseAdapter myDatabaseHelper; // this will be the access point for the
	// database
	Context myContext;

	/**
	 * Constructor. First, it initializes the DatabaseAdapter
	 * 
	 * @param myContext
	 *            the context (pass "this" from an activity)
	 */
	public XmlToDatabaseHelper(Context myContext) {

		System.out.println("XmlToDatabaseHelper being constructed...");
		// initialize database
		myDatabaseHelper = new DatabaseAdapter(myContext);
		this.myContext = myContext;

		// check for the existence of the version file

		/*
		 * Java.io.File is not supported for Android, and will break if the user
		 * does not have an SD card installed. The preferred way to do this type
		 * of check is via an Android preference.
		 */
		System.out.println("Checking for existence of version preference...");
		String version_file = this.myContext.getString(R.string.VERSION_FILE);
		String app_version = appVersion;

		SharedPreferences settings = this.myContext.getSharedPreferences(
				"athletics_database", this.myContext.MODE_PRIVATE);
		if (!settings.contains(version_file)) {
		    System.out.println("Version preference does not exist.");

		    SharedPreferences.Editor prefs = settings.edit();
		    prefs.putString(version_file, app_version);
		    prefs.commit();
		} else {
			System.out.println("Version file exists.");
		}
	}

	/**
	 * Opens the database connection using the DatabaseAdapter object
	 * initialized on construction.
	 */
	private void openDatabase() {
		// open the database connection
		try {
			myDatabaseHelper.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the database connection using the DatabaseAdapter object
	 * initialized on construction.
	 */
	private void closeDatabase() {
		try {
			myDatabaseHelper.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether or not the data in the database is up to date.
	 * 
	 * @return true if up to date, false if not
	 */
	public boolean isUpdated(Context myContext) {
		System.out.println("Checking if database is updated...");
		this.myContext = myContext;
		try {
			FileInputStream fileReader = myContext.openFileInput(myContext
					.getString(R.string.VERSION_FILE));
			BufferedReader onlineBufferedReader = new BufferedReader(
					new InputStreamReader(new URL(myContext
							.getString(R.string.VERSION_URL)).openStream()), 4);
			BufferedReader offlineBufferedReader = new BufferedReader(
					new InputStreamReader(fileReader), 4);
			String onlineVersion = onlineBufferedReader.readLine();
			String offlineVersion = offlineBufferedReader.readLine();
			if (onlineVersion.equalsIgnoreCase(offlineVersion) == false) {
				System.out.println("Database is not up to date.");
				return false;
			} else {
				System.out.println("Database is up to date.");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: make this function throw something else to signal an error
			return true;
		}
	}

	/**
	 * Updates the local copy of the version file.
	 * 
	 * @return true if successful, false if an exception is thrown
	 */
	private boolean updateVersion() {
		System.out.println("Attempting to update version file...");
		try {
			FileOutputStream versionOutput = myContext.openFileOutput(myContext
					.getString(R.string.VERSION_FILE), 0);
			PrintStream versionPrinter = new PrintStream(versionOutput);
			BufferedReader onlineVersionBufferedReader = new BufferedReader(
					new InputStreamReader(new URL(myContext
							.getString(R.string.VERSION_URL)).openStream()), 4);
			versionPrinter.println(onlineVersionBufferedReader);
			versionPrinter.close();
			System.out.println("Version file updated.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Parses the XML file online into a local dom object.
	 */
	private void parseXmlFile() {
		System.out.println("Parsing XML file to DOM object...");
		// create a DocumentBuilderFactory
		// documentation: "defines a factory API that enables applications to
		// obtain a parser
		// that produces DOM object trees from XML elements"
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// try to create a parser and create our Document "dom" using the
		// results of the parser
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(new URL
					(myContext.getString
							(R.string.DATA_URL))
					.openStream());
			System.out.println("XML file parsed to DOM object.");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace(); // catches parser configuration exceptions
		} catch (SAXException se) {
			se.printStackTrace(); // catches SAX parser exceptions (bad XML)
		} catch (IOException ioe) {
			ioe.printStackTrace(); // catches IO exceptions
		}
	}

	/**
	 * First: calls parseXmlFile() Second:Takes the DOM object, traverses it,
	 * and posts the results to the SQLite database
	 */
	public void updateDatabase() {
		System.out.println("Updating the database...");
		openDatabase();
		parseXmlFile();
		// get the root element, which will be the <data> tag
		Element rootElement = dom.getDocumentElement();

		System.out.println("Adding news items...");
		// get a nodelist of news items
		NodeList newsNodeList = rootElement.getElementsByTagName("news");

		// iterate through the nodelist and add each news to the database
		if (newsNodeList != null && newsNodeList.getLength() > 0) {
			for (int i = 0; i < newsNodeList.getLength(); i++) {
				// get the i(th) news item in the list
				Element el = (Element) newsNodeList.item(i);
				addNewsItemToDatabase(el);
			}
		}
		System.out.println("News items added.");
		System.out.println("Adding games...");

		// get a nodelist of games
		NodeList gameNodeList = rootElement.getElementsByTagName("game");

		// iterate through the nodelist and add each game to the database
		if (gameNodeList != null && gameNodeList.getLength() > 0) {
			for (int i = 0; i < gameNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) gameNodeList.item(i);
				addGameToDatabase(el);
			}
		}
		System.out.println("Games added.");

		System.out.println("Adding sports...");

		// get a nodelist of games
		NodeList sportNodeList = rootElement.getElementsByTagName("sport");

		// iterate through the nodelist and add each game to the database
		if (sportNodeList != null && sportNodeList.getLength() > 0) {
			for (int i = 0; i < sportNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) sportNodeList.item(i);
				addSportToDatabase(el);
			}
		}
		System.out.println("Sports added.");

		System.out.println("Adding conferences...");

		// get a nodelist of conferences
		NodeList conferenceNodeList = rootElement
				.getElementsByTagName("conference");

		// iterate through the nodelist and add each conference to the database
		if (conferenceNodeList != null && conferenceNodeList.getLength() > 0) {
			for (int i = 0; i < conferenceNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) conferenceNodeList.item(i);
				addConferenceToDatabase(el);
			}
		}
		System.out.println("Conferences added.");
		System.out.println("Adding teams...");

		// get a nodelist of teams
		NodeList teamNodeList = rootElement.getElementsByTagName("team");

		// iterate through the nodelist and add each team to the database
		if (teamNodeList != null && teamNodeList.getLength() > 0) {
			for (int i = 0; i < teamNodeList.getLength(); i++) {
				// get the i(th) team in the list
				Element el = (Element) teamNodeList.item(i);
				addTeamToDatabase(el);
			}
		}
		System.out.println("Teams added.");
		System.out.println("Adding players...");

		// get a nodelist of players
		NodeList playerNodeList = rootElement.getElementsByTagName("player");

		// iterate through the nodelist and add each player to the database
		if (playerNodeList != null && playerNodeList.getLength() > 0) {
			for (int i = 0; i < playerNodeList.getLength(); i++) {
				// get the i(th) player in the list
				Element el = (Element) playerNodeList.item(i);
				addPlayerToDatabase(el);
			}
		}
		System.out.println("Players added.");

		closeDatabase();
		updateVersion();
	}

	/**
	 * this function will add each game to the database
	 * 
	 * @param el
	 *            element from nodelist of games
	 */
	private void addGameToDatabase(Element el) {
		// for each game, we will get the following attributes
		int id = Integer.parseInt(el.getAttribute("game_id"));
		int hometeam = Integer.parseInt(el.getAttribute("hometeam"));
		int awayteam = Integer.parseInt(el.getAttribute("awayteam"));
		int sport = Integer.parseInt(el.getAttribute("sport"));
		String time = el.getAttribute("time");
		int homescore = Integer.parseInt(el.getAttribute("homescore"));
		int awayscore = Integer.parseInt(el.getAttribute("awayscore"));
		System.out.println("Adding game to database: " + awayteam + " @ "
				+ hometeam + "...");
		myDatabaseHelper.createGame(id, hometeam, awayteam, sport, time,
				homescore, awayscore);
		System.out.println("Added game to database.");
	}

	/**
	 * this function will add each team to the database
	 * 
	 * @param el
	 *            element from nodelist of teams
	 */
	private void addTeamToDatabase(Element el) {
		// for each team, get the following attributes
		int id = Integer.parseInt(el.getAttribute("team_id"));
		String school = el.getAttribute("school");
		String name = el.getAttribute("name");
		int conference = Integer.parseInt(el.getAttribute("conference"));

		System.out.println("Adding team to database: " + school + "...");

		myDatabaseHelper.createTeam(id, school, name, conference);
		System.out.println("Added team to database.");
	}

	/**
	 * this function will add each player to the database
	 * 
	 * @param team
	 *            the team the player comes from
	 * @param el
	 *            element from nodelist of players
	 */
	private void addPlayerToDatabase(Element el) {
		int id = Integer.parseInt(el.getAttribute("player_id"));
		String firstname = el.getAttribute("firstname");
		String lastname = el.getAttribute("lastname");
		int number = Integer.parseInt(el.getAttribute("number"));
		String position = el.getAttribute("position");
		int sport = Integer.parseInt(el.getAttribute("sport"));
		int team = Integer.parseInt(el.getAttribute("team"));

		System.out.println("Adding player to database: " + firstname + " "
				+ lastname + "...");

		myDatabaseHelper.createPlayer(id, firstname, lastname, number,
				position, sport, team);
		System.out.println("Added player to database.");
	}

	/**
	 * this function will add each news item to the database
	 * 
	 * @param el
	 *            element from nodelist of news items
	 */
	private void addNewsItemToDatabase(Element el) {
		// for each news item, we will get the following attributes
		int id = Integer.parseInt(el.getAttribute("news_id"));
		String title = el.getAttribute("title");
		String body = el.getAttribute("body");
		String link = el.getAttribute("link");
		String date = el.getAttribute("date");
		int sport = Integer.parseInt(el.getAttribute("sport"));

		System.out.println("Adding news item to database: "
				+ title.substring(0, 15) + "...");

		myDatabaseHelper.createNewsItem(id, title, body, link, date, sport);
		System.out.println("Added news item to database.");

	}

	private void addConferenceToDatabase(Element el) {
		// for each conference, we will get the following attributes
		int id = Integer.parseInt(el.getAttribute("conference_id"));
		String name = el.getAttribute("name");
		String abbreviation = el.getAttribute("abbreviation");

		System.out.println("Adding conference to database: " + abbreviation
				+ "...");

		myDatabaseHelper.createConference(id, name, abbreviation);
		System.out.println("Added conference to database.");

	}

	private void addSportToDatabase(Element el) {
		// for each team, we will get the following attributes
		int id = Integer.parseInt(el.getAttribute("sport_id"));
		String name = el.getAttribute("name");

		System.out.println("Adding sport to database: " + name + "...");

		myDatabaseHelper.createSport(id, name);
		System.out.println("Added sport to database.");

	}
}