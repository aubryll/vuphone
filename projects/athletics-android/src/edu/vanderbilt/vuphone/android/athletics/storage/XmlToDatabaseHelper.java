package edu.vanderbilt.vuphone.android.athletics.storage;

// mmm
import java.io.BufferedReader;
import java.io.File;
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

		// check for the existence of the version file

		System.out.println("Checking for existence of version file...");
		File versionFile = new File(myContext.getString(R.string.VERSION_FILE));
		if (!versionFile.exists()) {
			System.out.println("Version file does not exist.");
			try {
				FileOutputStream versionOutput = myContext.openFileOutput(
						myContext.getString(R.string.VERSION_FILE), 0);
				PrintStream versionPrinter = new PrintStream(versionOutput);
				versionPrinter.println("0");
				versionPrinter.close();
				versionOutput.close();
				System.out.println("Version file created.");
			} catch (Exception f) {
				f.printStackTrace();
			}
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
			// TODO: make this function return something else to signal an error
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
			dom = db.parse(new URL(myContext.getString(R.string.DATA_URL))
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
		NodeList newsNodeList = rootElement.getElementsByTagName(myContext
				.getString(R.string.XML_TAG_NEWS));

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
		NodeList gameNodeList = rootElement.getElementsByTagName(myContext
				.getString(R.string.XML_TAG_GAME));

		// iterate through the nodelist and add each game to the database
		if (gameNodeList != null && gameNodeList.getLength() > 0) {
			for (int i = 0; i < gameNodeList.getLength(); i++) {
				// get the i(th) game in the list
				Element el = (Element) gameNodeList.item(i);
				addGameToDatabase(el);
			}
		}
		System.out.println("Games added.");
		System.out.println("Adding teams and players...");

		// get a nodelist of teams
		NodeList teamNodeList = rootElement.getElementsByTagName(myContext
				.getString(R.string.XML_TAG_TEAM));

		// iterate through the nodelist and add each team to the database
		if (teamNodeList != null && teamNodeList.getLength() > 0) {
			for (int i = 0; i < teamNodeList.getLength(); i++) {
				// get the i(th) team in the list
				Element el = (Element) teamNodeList.item(i);
				addTeamToDatabase(el);

				// build a nodelist for players
				NodeList playerNodeList = el.getElementsByTagName(myContext
						.getString(R.string.XML_TAG_PLAYER));

				// iterate through the nodelist and add each player to the
				// database
				if (playerNodeList != null && playerNodeList.getLength() > 0) {
					for (int j = 0; j < playerNodeList.getLength(); j++) {
						// get the i(th) player in the team
						Element e = (Element) playerNodeList.item(j);
						addPlayerToDatabase(el.getAttribute(myContext
								.getString(R.string.KEY_TEAMS_NAME)), e);
					}
				}
			}
		}
		System.out.println("Teams and players added.");
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
		String hometeam = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_HOMETEAM));
		String awayteam = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_AWAYTEAM));
		String sport = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_SPORT));
		String type = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_TYPE));
		String time = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_TIME));
		String homescore = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_HOMESCORE));
		String awayscore = el.getAttribute(myContext
				.getString(R.string.KEY_GAMES_AWAYSCORE));
		System.out.println("Adding game to database: " + awayteam + " @ "
				+ hometeam + "...");

		// add this game to the database if it does not exist
		// existence is checked by comparing the two teams and the time
		Cursor gameList = myDatabaseHelper.fetchAllGames();
		int awayindex = gameList.getColumnIndex(myContext
				.getString(R.string.KEY_GAMES_AWAYTEAM));
		int homeindex = gameList.getColumnIndex(myContext
				.getString(R.string.KEY_GAMES_HOMETEAM));
		int timeindex = gameList.getColumnIndex(myContext
				.getString(R.string.KEY_GAMES_TIME));
		int rowindex = gameList.getColumnIndex(myContext
				.getString(R.string.KEY_GAMES_ROWID));
		boolean exists = false;
		if (gameList.moveToFirst()) {
			while (gameList.isAfterLast() == false) {
				int currentRow = gameList.getInt(rowindex);
				if (gameList.getString(timeindex).equalsIgnoreCase(time)) {
					if (gameList.getString(homeindex)
							.equalsIgnoreCase(hometeam)) {
						if (gameList.getString(awayindex).equalsIgnoreCase(
								awayteam)) {
							myDatabaseHelper.updateGame(currentRow, hometeam,
									awayteam, sport, type, time, homescore,
									awayscore);
							exists = true;
							System.out.println("Updated game to database.");
							break;
						}
					}
				}
				gameList.moveToNext();
			}
		}
		if (exists == false) {
			myDatabaseHelper.createGame(hometeam, awayteam, sport, type, time,
					homescore, awayscore);
			System.out.println("Added game to database.");
		}
		gameList.close();

	}

	/**
	 * this function will add each team to the database
	 * 
	 * @param el
	 *            element from nodelist of teams
	 */
	private void addTeamToDatabase(Element el) {
		// for each game, we will get the following attributes
		String school = el.getAttribute(myContext
				.getString(R.string.KEY_TEAMS_SCHOOL));
		String name = el.getAttribute(myContext
				.getString(R.string.KEY_TEAMS_NAME));
		String conference = el.getAttribute(myContext
				.getString(R.string.KEY_TEAMS_CONFERENCE));

		System.out.println("Adding team to database: " + school + "...");
		// add this team to the database if it does not exist
		// existence is checked by looking for the school
		Cursor teamList = myDatabaseHelper.fetchAllTeams();
		int schoolindex = teamList.getColumnIndex(myContext
				.getString(R.string.KEY_TEAMS_SCHOOL));
		int rowindex = teamList.getColumnIndex(myContext
				.getString(R.string.KEY_TEAMS_ROWID));
		boolean exists = false;
		if (teamList.moveToFirst()) {
			while (teamList.isAfterLast() == false) {
				int currentRow = teamList.getInt(rowindex);
				if (teamList.getString(schoolindex).equalsIgnoreCase(school)) {
					myDatabaseHelper.updateTeam(currentRow, school, name,
							conference);
					exists = true;
					System.out.println("Updated team to database.");
				}
				teamList.moveToNext();
			}
		}
		if (exists == false) {
			myDatabaseHelper.createTeam(school, name, conference);
			System.out.println("Added team to database.");
		}
		teamList.close();
	}

	/**
	 * this function will add each player to the database
	 * 
	 * @param team
	 *            the team the player comes from
	 * @param el
	 *            element from nodelist of players
	 */
	private void addPlayerToDatabase(String team, Element el) {
		String name = el.getAttribute(myContext
				.getString(R.string.KEY_PLAYERS_NAME));
		String position = el.getAttribute(myContext
				.getString(R.string.KEY_PLAYERS_POSITION));
		String number = el.getAttribute(myContext
				.getString(R.string.KEY_PLAYERS_NUMBER));

		System.out.println("Adding player to database: " + name + "...");
		// add this player to the database if he/she does not exist
		// existence is checked by looking for the name and team
		Cursor playerList = myDatabaseHelper.fetchAllPlayers();
		int nameindex = playerList.getColumnIndex(myContext
				.getString(R.string.KEY_PLAYERS_NAME));
		int teamindex = playerList.getColumnIndex(myContext
				.getString(R.string.KEY_PLAYERS_TEAM));
		int rowindex = playerList.getColumnIndex(myContext
				.getString(R.string.KEY_PLAYERS_ROWID));
		boolean exists = false;
		if (playerList.moveToFirst()) {
			while (playerList.isAfterLast() == false) {
				int currentRow = playerList.getInt(rowindex);
				if (playerList.getString(nameindex).equalsIgnoreCase(name)) {
					if (playerList.getString(teamindex).equalsIgnoreCase(team)) {
						myDatabaseHelper.updatePlayer(currentRow, name, team,
								position, number);
						exists = true;
						System.out.println("Updated player to database.");
					}
				}
				playerList.moveToNext();
			}
		}

		if (exists == false) {
			myDatabaseHelper.createPlayer(name, team, position, number);
			System.out.println("Added player to database.");
		}
		playerList.close();
	}

	/**
	 * this function will add each news item to the database
	 * 
	 * @param el
	 *            element from nodelist of news items
	 */
	private void addNewsItemToDatabase(Element el) {
		// for each news item, we will get the following attributes
		String title = el.getAttribute(myContext
				.getString(R.string.KEY_NEWS_TITLE));
		String body = el.getAttribute(myContext
				.getString(R.string.KEY_NEWS_BODY));
		String link = el.getAttribute(myContext
				.getString(R.string.KEY_NEWS_LINK));

		System.out.println("Adding news item to database: "
				+ title.substring(0, 15) + "...");
		// add this team to the database if it does not exist
		// existence is checked by looking for the title
		Cursor newsList = myDatabaseHelper.fetchAllNewsItems();
		int titleindex = newsList.getColumnIndex(myContext
				.getString(R.string.KEY_NEWS_TITLE));
		int rowindex = newsList.getColumnIndex(myContext
				.getString(R.string.KEY_NEWS_ROWID));
		boolean exists = false;
		if (newsList.moveToFirst()) {
			while (newsList.isAfterLast() == false) {
				int currentRow = newsList.getInt(rowindex);
				if (newsList.getString(titleindex).equalsIgnoreCase(title)) {
					myDatabaseHelper.updateNewsItem(currentRow, title, body,
							link);
					exists = true;
					System.out.println("Updated news item to database.");
				}
				newsList.moveToNext();
			}
		}

		if (exists == false) {
			myDatabaseHelper.createNewsItem(title, body, link);
			System.out.println("Added news item to database.");
		}
		newsList.close();
	}
}