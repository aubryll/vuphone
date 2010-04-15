package edu.vanderbilt.vuphone.android.athletics.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import android.database.SQLException;

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
	private static String dataVersion = "data.version";
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

		SharedPreferences settings = this.myContext.getSharedPreferences(
				"athletics_database", Context.MODE_PRIVATE);
		if (!settings.contains(dataVersion)) {
			System.out.println("Version preference does not exist.");

			SharedPreferences.Editor prefs = settings.edit();
			prefs.putString(dataVersion, "0");
			prefs.commit();
		} else {
			System.out.println("Version preference exists.");
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
		BufferedReader onlineBufferedReader;
		try {
			onlineBufferedReader = new BufferedReader(new InputStreamReader(
					new URL(versionURL).openStream()));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			System.out
					.println("URL error occurred when reading online version file.");
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out
					.println("IO error occurred when reading online version file.");
			return false;
		}
		SharedPreferences settings = this.myContext.getSharedPreferences(
				"athletics_database", Context.MODE_PRIVATE);
		String onlineVersion;
		try {
			onlineVersion = onlineBufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out
					.println("IO error occurred when reading online version file.");
			return false;
		}
		String offlineVersion = settings.getString(dataVersion, "0");
		if (onlineVersion.equalsIgnoreCase(offlineVersion)) {
			System.out.println("Database is up to date.");
			return true;
		} else {
			System.out
					.println("Database is not up to date or an error occurred.");
			return false;
		}
	}

	/**
	 * Updates the local copy of the version file.
	 * 
	 * @return true if successful, false if an exception is thrown
	 */
	private boolean updateVersion() {
		System.out.println("Attempting to update version file...");
		SharedPreferences settings = this.myContext.getSharedPreferences(
				"athletics_database", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefs = settings.edit();

		BufferedReader onlineVersionBufferedReader;
		try {
			onlineVersionBufferedReader = new BufferedReader(
					new InputStreamReader(new URL(versionURL).openStream()));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			System.out
					.println("URL error occurred when reading online version file.");
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out
					.println("IO error occurred when reading online version file.");
			return false;
		}
		try {
			prefs
					.putString(dataVersion, onlineVersionBufferedReader
							.readLine());
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out
					.println("IO error occurred when reading online version file.");
			return false;
		}
		prefs.commit();
		System.out.println("Version updated.");
		return true;
	}

	/**
	 * Parses the XML file online into a local dom object.
	 */
	private boolean parseXmlFile() {
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
			dom = db.parse(new URL(dataURL).openStream());
			System.out.println("XML file parsed to DOM object.");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			System.out.println("Parser Configuration error");
			return false;
		} catch (SAXException se) {
			se.printStackTrace();
			System.out.println("XML error");
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IO error");
			return false;
		}
		return true;
	}

	public boolean addItems(Element rootElement, String tag) {
		NodeList itemNodeList = rootElement.getElementsByTagName(tag);
		// iterate through the nodelist and add each item to the database
		if (itemNodeList != null && itemNodeList.getLength() > 0) {
			for (int i = 0; i < itemNodeList.getLength(); i++) {
				// get the i(th) item in the list
				Element el = (Element) itemNodeList.item(i);
				addItemToDatabase(el, tag);
			}
		} else {
			return false;
		}
		return true;
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

		addItems(rootElement, "news");
		addItems(rootElement, "game");
		addItems(rootElement, "sport");
		addItems(rootElement, "conference");
		addItems(rootElement, "team");
		addItems(rootElement, "player");

		closeDatabase();
		updateVersion();
	}

	/**
	 * this function will add each game to the database
	 * 
	 * @param el
	 *            element from nodelist of games
	 */
	private boolean addItemToDatabase(Element el, String tag) {
		long row = -1;
		if (tag.equalsIgnoreCase("news")) {
			int id = Integer.parseInt(el.getAttribute("news_id"));
			String title = el.getAttribute("title");
			String body = el.getAttribute("body");
			String link = el.getAttribute("link");
			String date = el.getAttribute("date");
			int sport = Integer.parseInt(el.getAttribute("sport"));
			row = myDatabaseHelper.createNewsItem(id, title, body, link, date,
					sport);
		} else if (tag.equalsIgnoreCase("game")) {
			int id = Integer.parseInt(el.getAttribute("game_id"));
			int hometeam = Integer.parseInt(el.getAttribute("hometeam"));
			int awayteam = Integer.parseInt(el.getAttribute("awayteam"));
			int sport = Integer.parseInt(el.getAttribute("sport"));
			String time = el.getAttribute("time");
			int homescore = Integer.parseInt(el.getAttribute("homescore"));
			int awayscore = Integer.parseInt(el.getAttribute("awayscore"));
			row = myDatabaseHelper.createGame(id, hometeam, awayteam, sport,
					time, homescore, awayscore);
		} else if (tag.equalsIgnoreCase("sport")) {
			int id = Integer.parseInt(el.getAttribute("sport_id"));
			String name = el.getAttribute("name");
			row = myDatabaseHelper.createSport(id, name);
		} else if (tag.equalsIgnoreCase("conference")) {
			int id = Integer.parseInt(el.getAttribute("conference_id"));
			String name = el.getAttribute("name");
			String abbreviation = el.getAttribute("abbreviation");
			row = myDatabaseHelper.createConference(id, name, abbreviation);
		} else if (tag.equalsIgnoreCase("team")) {
			int id = Integer.parseInt(el.getAttribute("team_id"));
			String school = el.getAttribute("school");
			String name = el.getAttribute("name");
			int conference = Integer.parseInt(el.getAttribute("conference"));
			row = myDatabaseHelper.createTeam(id, school, name, conference);
		} else if (tag.equalsIgnoreCase("player")) {
			int id = Integer.parseInt(el.getAttribute("player_id"));
			String firstname = el.getAttribute("firstname");
			String lastname = el.getAttribute("lastname");
			int number = Integer.parseInt(el.getAttribute("number"));
			String position = el.getAttribute("position");
			int sport = Integer.parseInt(el.getAttribute("sport"));
			int team = Integer.parseInt(el.getAttribute("team"));
			row = myDatabaseHelper.createPlayer(id, firstname, lastname,
					number, position, sport, team);
		}
		if (row == -1) {
			System.out.println("Error occurred on inserting a " + tag);
			return false;
		} else {
			System.out.println(tag + "added with row id " + row);
			return true;
		}
	}
}