package edu.vanderbilt.vuphone.android.athletics.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper class. To use the SQLite database, simply create a helper and
 * call the open() method. call the close() on destruction
 * 
 * @author Zach McCormick
 */
public class DatabaseAdapter {

	// this is a private object extended/defined later
	private DatabaseHelper myDatabaseHelper;

	// create a SQLiteDatabase object
	private SQLiteDatabase myDatabase;

	// define the database version
	private static final int DATABASE_VERSION = 10;

	// constant for the context (for the SQLiteOpenHelper)
	private final Context myContext;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, "data", null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db
					.execSQL("CREATE TABLE conferences(conference_id integer PRIMARY KEY, name text, abbreviation text)");
			db
					.execSQL("CREATE TABLE teams(team_id integer PRIMARY KEY, school text, name text, conference integer, CONSTRAINT conference_fk FOREIGN KEY (conference) REFERENCES conferences(conference_id))");
			db
					.execSQL("CREATE TABLE sports( sport_id integer PRIMARY KEY, name text)");
			db
					.execSQL("CREATE TABLE players( player_id integer PRIMARY KEY, number integer, position text, firstname text, lastname text, team integer, sport integer, CONSTRAINT team_fk FOREIGN KEY (team) REFERENCES teams(team_id), CONSTRAINT sport_fk FOREIGN KEY (sport) REFERENCES sports(sport_id))");
			db
					.execSQL("CREATE TABLE games( game_id integer PRIMARY KEY, hometeam integer, awayteam integer, sport integer, time text, homescore integer, awayscore integer, CONSTRAINT hometeam_fk FOREIGN KEY (hometeam) REFERENCES teams(team_id), CONSTRAINT awayteam_fk FOREIGN KEY (awayteam) REFERENCES teams(team_id), CONSTRAINT sport_fk FOREIGN KEY (sport) REFERENCES sport(sport_id))");
			db
					.execSQL("CREATE TABLE news( news_id integer PRIMARY KEY, title text, body text, link text, date text, sport integer, CONSTRAINT sport_fk FOREIGN KEY (sport) REFERENCES sports(sport_id))");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("DatabaseAdapter", "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + "conferences");
			db.execSQL("DROP TABLE IF EXISTS " + "teams");
			db.execSQL("DROP TABLE IF EXISTS " + "sports");
			db.execSQL("DROP TABLE IF EXISTS " + "players");
			db.execSQL("DROP TABLE IF EXISTS " + "games");
			db.execSQL("DROP TABLE IF EXISTS " + "news");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DatabaseAdapter(Context ctx) {
		System.out.println("Creating DatabaseAdapter...");
		this.myContext = ctx;
		System.out.println("DatabaseAdapter created.");
		this.open();
	}

	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}

	/**
	 * Opens the database. If it cannot be opened, try to create a new instance
	 * of the database. If it cannot be created, throw an exception to signal
	 * the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DatabaseAdapter open() throws SQLException {
		System.out.println("Opening the database...");
		myDatabaseHelper = new DatabaseHelper(myContext);
		myDatabase = myDatabaseHelper.getWritableDatabase();
		System.out.println("Database opened.");
		return this;
	}

	/**
	 * Closes the database
	 */
	public void close() {
		System.out.println("Closing the database...");
		myDatabaseHelper.close();
		System.out.println("Database closed.");
	}

	/**
	 * Create a new news item using the title, body, and link provided. If the
	 * news item is successfully created, return the new rowId for that news
	 * item, otherwise return a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @param link
	 *            the link to the news page
	 * @return rowId or -1 if failed
	 */
	public long createNewsItem(long id, String title, String body, String link,
			String date, int sport) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("news_id", id);
		initialValues.put("title", title);
		initialValues.put("body", body);
		initialValues.put("link", link);
		initialValues.put("date", date);
		initialValues.put("sport", sport);
		long x = myDatabase.insert("news", null, initialValues);
		System.out.println("Creating news item with title: " + title + "...");
		if (x == -1) {
			System.out.println("NEWS ITEM CREATION FAILED");
		} else {
			System.out.println("News item created at row: " + x);
		}
		return x;
	}

	/**
	 * Create a new game using the data provided. If the game is successfully
	 * created, return the new rowId for that game, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param hometeam
	 *            the home team of the game
	 * @param awayteam
	 *            the away team of the game
	 * @param sport
	 *            the sport of the game (Men's Basketball, Women's Lacrosse,
	 *            etc)
	 * @param type
	 *            the type of game (conference, scrimmage, playoff, season, etc)
	 * @param time
	 *            the time of the game (YYYY-MM-DD HH:MM:SS.SSS)
	 * @param homescore
	 *            the score of the home team (use an empty string if unplayed)
	 * @param awayscore
	 *            the score of the away team (use an empty string if unplayed)
	 * @return rowId or -1 if failed
	 */
	public long createGame(long id, int hometeam, int awayteam, int sport,
			String time, int homescore, int awayscore) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("game_id", id);
		initialValues.put("hometeam", hometeam);
		initialValues.put("awayteam", awayteam);
		initialValues.put("sport", sport);
		initialValues.put("time", time);
		initialValues.put("homescore", homescore);
		initialValues.put("awayscore", awayscore);

		System.out.println("Creating game: " + awayteam + " @ " + hometeam
				+ "...");
		long x = myDatabase.insert("games", null, initialValues);
		if (x == -1) {
			System.out.println("GAME CREATION FAILED");
		} else {
			System.out.println("Game created at row: " + x);

		}
		return x;
	}

	/**
	 * Create a new player using the data provided. If the player is
	 * successfully created, return the new rowId for that player, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param name
	 *            the name of the player
	 * @param team
	 *            the team of the player
	 * @param position
	 *            the position of the player (empty string if not applicable)
	 * @param number
	 *            the number of the player (empty string if not applicable)
	 * @return rowId or -1 if failed
	 */
	public long createPlayer(long id, String firstname, String lastname,
			int number, String position, int sport, int team) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("player_id", id);
		initialValues.put("firstname", firstname);
		initialValues.put("lastname", lastname);
		initialValues.put("number", number);
		initialValues.put("position", position);
		initialValues.put("sport", sport);
		initialValues.put("team", team);

		System.out.println("Creating player: " + firstname + " " + lastname
				+ "...");
		long x = myDatabase.insert("players", null, initialValues);
		if (x == -1) {
			System.out.println("PLAYER CREATION FAILED");
		} else {
			System.out.println("Player created at row: " + x);

		}
		return x;
	}

	/**
	 * Create a new team using the data provided. If the team is successfully
	 * created, return the new rowId for that team, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param school
	 *            the school name of the team (Vanderbilt, Kentucky, etc)
	 * @param name
	 *            the name of the team (Commodores, Wildcats, etc)
	 * @param conference
	 *            the conference of the team (SEC, Big 10, etc)
	 * @return rowId or -1 if failed
	 */
	public long createTeam(long id, String school, String name, int conference) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", id);
		initialValues.put("school", school);
		initialValues.put("name", name);
		initialValues.put("conference", conference);

		System.out.println("Creating team: " + school + " " + name + "...");
		long x = myDatabase.insert("teams", null, initialValues);
		if (x == -1) {
			System.out.println("TEAM CREATION FAILED");
		} else {
			System.out.println("Team created at row: " + x);
		}
		return x;

	}

	public long createSport(long id, String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", id);
		initialValues.put("name", name);

		System.out.println("Creating sport: " + name + "...");
		long x = myDatabase.insert("sports", null, initialValues);
		if (x == -1) {
			System.out.println("SPORT CREATION FAILED");
		} else {
			System.out.println("Sport created at row: " + x);
		}
		return x;

	}

	public long createConference(long id, String name, String abbreviation) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("id", id);
		initialValues.put("name", name);
		initialValues.put("abbreviation", abbreviation);

		System.out.println("Creating conference: " + name + "...");
		long x = myDatabase.insert("conference", null, initialValues);
		if (x == -1) {
			System.out.println("CONFERENCE CREATION FAILED");
		} else {
			System.out.println("Conference created at row: " + x);
		}
		return x;

	}

	/**
	 * Delete the news item with the given rowId
	 * 
	 * @param rowId
	 *            id of news item to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNewsItem(long id) {
		System.out.println("Deleting news item " + id + "...");
		int x = myDatabase.delete("news", "news_id" + "=" + id, null);
		System.out.println("Deleted " + x + " news item(s).");
		return x > 0;
	}

	/**
	 * Delete the game with the given rowId
	 * 
	 * @param rowId
	 *            id of game to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteGame(long id) {
		System.out.println("Deleting game " + id + "...");
		int x = myDatabase.delete("games", "game_id" + "=" + id, null);
		System.out.println("Deleted " + x + " game(s).");
		return x > 0;
	}

	/**
	 * Delete the player with the given rowId
	 * 
	 * @param rowId
	 *            id of player to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePlayer(long id) {
		System.out.println("Deleting player " + id + "...");
		int x = myDatabase.delete("players", "player_id" + "=" + id, null);
		System.out.println("Deleted " + x + " player(s).");
		return x > 0;
	}

	/**
	 * Delete the team with the given rowId
	 * 
	 * @param rowId
	 *            id of team to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTeam(long id) {
		System.out.println("Deleting team " + id + "...");
		int x = myDatabase.delete("teams", "team_id" + "=" + id, null);
		System.out.println("Deleted " + x + " team(s).");
		return x > 0;
	}

	public boolean deleteSport(long id) {
		System.out.println("Deleting sport " + id + "...");
		int x = myDatabase.delete("sports", "sport_id" + "=" + id, null);
		System.out.println("Deleted " + x + " sport(s).");
		return x > 0;
	}

	public boolean deleteConference(long id) {
		System.out.println("Deleting conference " + id + "...");
		int x = myDatabase.delete("conferences", "conference_id" + "=" + id,
				null);
		System.out.println("Deleted " + x + " conference(s).");
		return x > 0;
	}

	/**
	 * Return a Cursor over the list of all news items in the database
	 * 
	 * @return Cursor over all news items
	 */
	public Cursor fetchAllNewsItems() {

		System.out.println("Fetching all news items...");
		return myDatabase
				.rawQuery(
						"SELECT games.game_id, (SELECT teams.name FROM teams WHERE games.hometeam=teams.team_id) AS hometeam, (SELECT teams.name FROM teams WHERE games.awayteam=teams.team_id) AS awayteam, (SELECT sports.name FROM sports WHERE games.sport=sports.sport_id) AS sport, games.time, games.homescore, games.awayscore FROM games",
						null);
	}

	/**
	 * Return a Cursor over a list of filtered news items in the database
	 * 
	 * @param orderBy
	 *            a SQL string for ordering the data
	 * @param where
	 *            a SQL string that would go after "WHERE " in a command
	 * @return Cursor over the news items
	 */
	public Cursor fetchFilteredNewsItems(String where, String orderBy) {
		String[] selectionArgs = new String[2];
		selectionArgs[0] = where;
		selectionArgs[1] = orderBy;
		System.out.println("Fetching filtered news items...");
		return myDatabase
				.rawQuery(
						"SELECT news.news_id, news.title, news.body, news.link, news.date, (SELECT sports.name FROM sports WHERE news.sport=sports.sport_id) AS sport FROM news WHERE ? ORDER BY ?",
						selectionArgs);
	}

	/**
	 * Return a Cursor over the list of all games in the database
	 * 
	 * @return Cursor over all games
	 */
	public Cursor fetchAllGames() {

		System.out.println("Fetching all games...");
		return myDatabase
				.rawQuery(
						"SELECT games.game_id, (SELECT teams.name FROM teams WHERE games.hometeam=teams.team_id) AS hometeam, (SELECT teams.name FROM teams WHERE games.awayteam=teams.team_id) AS awayteam, (SELECT sports.name FROM sports WHERE games.sport=sports.sport_id) AS sport, games.time, games.homescore, games.awayscore FROM games",
						null);
	}

	/**
	 * Return a Cursor over the list of filtered games in the database
	 * 
	 * @param orderBy
	 *            a SQL string for ordering the data
	 * @param where
	 *            a SQL string that would go after "WHERE " in a command
	 * @return Cursor over filtered games
	 */
	public Cursor fetchFilteredGames(String where, String orderBy) {
		String[] selectionArgs = new String[2];
		selectionArgs[0] = where;
		selectionArgs[1] = orderBy;
		System.out.println("Fetching filtered games...");
		return myDatabase
				.rawQuery(
						"SELECT games.game_id, (SELECT teams.name FROM teams WHERE games.hometeam=teams.team_id) AS hometeam, (SELECT teams.name FROM teams WHERE games.awayteam=teams.team_id) AS awayteam, (SELECT sports.name FROM sports WHERE games.sport=sports.sport_id) AS sport, games.time, games.homescore, games.awayscore FROM games WHERE ? ORDER BY ?",
						selectionArgs);
	}

	/**
	 * Return a Cursor over the list of all players in the database
	 * 
	 * @return Cursor over all players
	 */
	public Cursor fetchAllPlayers() {

		System.out.println("Fetching all players...");
		return myDatabase
				.rawQuery(
						"SELECT players.player_id, players.firstname, players.lastname, players.number, players.position, (SELECT sports.name FROM sports WHERE players.sport=sports.sport_id) AS sport, (SELECT teams.name FROM teams WHERE players.team=team.team_id) AS team FROM players",
						null);
	}

	/**
	 * Return a Cursor over the list of all players in the database
	 * 
	 * @param orderBy
	 *            a SQL string for ordering the data
	 * @param where
	 *            a SQL string that would go after "WHERE " in a command
	 * @return Cursor over filtered players
	 */
	public Cursor fetchFilteredPlayers(String where, String orderBy) {
		String[] selectionArgs = new String[2];
		selectionArgs[0] = where;
		selectionArgs[1] = orderBy;
		System.out.println("Fetching filtered players...");
		return myDatabase
				.rawQuery(
						"SELECT players.player_id, players.firstname, players.lastname, players.number, players.position, (SELECT sports.name FROM sports WHERE players.sport=sports.sport_id) AS sport, (SELECT teams.name FROM teams WHERE players.team=team.team_id) AS team FROM players WHERE ? ORDER BY ?",
						selectionArgs);
	}

	/**
	 * Return a Cursor over the list of all teams in the database
	 * 
	 * @return Cursor over all teams
	 */
	public Cursor fetchAllTeams() {

		System.out.println("Fetching all teams...");
		return myDatabase
				.rawQuery(
						"SELECT teams.team_id, teams.school, teams.name, (SELECT conferences.name FROM conferences WHERE teams.conference=conferences.conference_id) AS conference FROM teams",
						null);
	}

	/**
	 * Return a Cursor over the list of filtered teams in the database
	 * 
	 * @param orderBy
	 *            a SQL string for ordering the data
	 * @param where
	 *            a SQL string that would go after "WHERE " in a command
	 * @return Cursor over filtered teams
	 */
	public Cursor fetchFilteredTeams(String where, String orderBy) {
		String[] selectionArgs = new String[2];
		selectionArgs[0] = where;
		selectionArgs[1] = orderBy;
		System.out.println("Fetching filtered teams...");
		return myDatabase
				.rawQuery(
						"SELECT teams.team_id, teams.school, teams.name, (SELECT conferences.name FROM conferences WHERE teams.conference=conferences.conference_id) AS conference FROM teams WHERE ? ORDER BY ?",
						selectionArgs);
	}

	/**
	 * Return a Cursor positioned at the news item that matches the given rowId
	 * 
	 * @param rowId
	 *            id of news item to retrieve
	 * @return Cursor positioned to matching news item, if found
	 * @throws SQLException
	 *             if news item could not be found/retrieved
	 */
	public Cursor fetchNewsItem(long id) throws SQLException {
		System.out.println("Fetching news item " + id + "...");
		Cursor myCursor = myDatabase.query(true, "news", null, "news_id" + "="
				+ id, null, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Return a Cursor positioned at the game that matches the given rowId
	 * 
	 * @param rowId
	 *            id of game to retrieve
	 * @return Cursor positioned to matching game, if found
	 * @throws SQLException
	 *             if game could not be found/retrieved
	 */
	public Cursor fetchGame(long id) throws SQLException {
		System.out.println("Fetching game " + id + "...");
		Cursor myCursor = myDatabase.query(true, "games", null, "game_id" + "="
				+ id, null, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Return a Cursor positioned at the player that matches the given rowId
	 * 
	 * @param rowId
	 *            id of player to retrieve
	 * @return Cursor positioned to matching player, if found
	 * @throws SQLException
	 *             if player could not be found/retrieved
	 */
	public Cursor fetchPlayer(long id) throws SQLException {
		System.out.println("Fetching player " + id + "...");

		Cursor myCursor = myDatabase.query(true, "players", null, "player_id"
				+ "=" + id, null, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Return a Cursor positioned at the player that matches the given rowId
	 * 
	 * @param rowId
	 *            id of player to retrieve
	 * @return Cursor positioned to matching player, if found
	 * @throws SQLException
	 *             if player could not be found/retrieved
	 */
	public Cursor fetchTeam(long id) throws SQLException {
		System.out.println("Fetching team " + id + "...");

		Cursor myCursor = myDatabase.query(true, "teams", null, "team_id" + "="
				+ id, null, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Executes the inputted raw SQL query
	 * 
	 * @param sqlQuery
	 *            SQL query string (don't include a ; at the end)
	 * @return Cursor over data
	 * @throws SQLException
	 *             if the SQL was invalid
	 */
	public Cursor executeSqlQuery(String sqlQuery) throws SQLException {
		System.out.println("EXECUTING ARBITRARY SQL QUERY: " + sqlQuery);

		Cursor myCursor = myDatabase.rawQuery(sqlQuery, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Update the news item using the details provided. The news item to be
	 * updated is specified using the rowId, and it is altered to use the data
	 * passed in
	 * 
	 * @param rowId
	 *            id of news item to update
	 * @param title
	 *            value to set news item title to
	 * @param body
	 *            value to set news item body to
	 * @param link
	 *            value to set news item link to
	 * @return true if the news item was successfully updated, false otherwise
	 */
	/*
	 * public boolean updateNewsItem(long rowId, String title, String body,
	 * String link) { ContentValues newValues = new ContentValues();
	 * newValues.put(myContext.getString(R.string.KEY_NEWS_TITLE), title);
	 * newValues.put(myContext.getString(R.string.KEY_NEWS_BODY), body);
	 * newValues.put(myContext.getString(R.string.KEY_NEWS_LINK), link);
	 * System.out.println("Updating news item at row " + rowId + "..."); int x =
	 * myDatabase.update(myContext .getString(R.string.DATABASE_TABLE_NEWS),
	 * newValues, myContext .getString(R.string.KEY_NEWS_ROWID) + "=" + rowId,
	 * null); System.out.println("Updated " + x + " news items."); return x > 0;
	 * }
	 */

	/**
	 * Update the game using the details provided. The game to be updated is
	 * specified using the rowId, and it is altered to use the data passed in
	 * 
	 * @param rowId
	 *            id of game to update
	 * @param hometeam
	 *            the home team of the game
	 * @param awayteam
	 *            the away team of the game
	 * @param sport
	 *            the sport of the game (Men's Basketball, Women's Lacrosse,
	 *            etc)
	 * @param type
	 *            the type of game (conference, scrimmage, playoff, season, etc)
	 * @param time
	 *            the time of the game (YYYY-MM-DD HH:MM:SS.SSS)
	 * @param homescore
	 *            the score of the home team (use an empty string if unplayed)
	 * @param awayscore
	 *            the score of the away team (use an empty string if unplayed)
	 * 
	 * @return true if the game was successfully updated, false otherwise
	 */
	/*
	 * public boolean updateGame(long rowId, String hometeam, String awayteam,
	 * String sport, String type, String time, String homescore, String
	 * awayscore) { ContentValues newValues = new ContentValues();
	 * newValues.put(myContext.getString(R.string.KEY_GAMES_HOMETEAM),
	 * hometeam);
	 * newValues.put(myContext.getString(R.string.KEY_GAMES_AWAYTEAM),
	 * awayteam); newValues.put(myContext.getString(R.string.KEY_GAMES_SPORT),
	 * sport); newValues.put(myContext.getString(R.string.KEY_GAMES_TYPE),
	 * type); newValues.put(myContext.getString(R.string.KEY_GAMES_TIME), time);
	 * newValues.put(myContext.getString(R.string.KEY_GAMES_HOMESCORE),
	 * homescore);
	 * newValues.put(myContext.getString(R.string.KEY_GAMES_AWAYSCORE),
	 * awayscore); System.out.println("Updating game at row " + rowId + "...");
	 * int x = myDatabase.update(myContext
	 * .getString(R.string.DATABASE_TABLE_GAMES), newValues, myContext
	 * .getString(R.string.KEY_GAMES_ROWID) + "=" + rowId, null);
	 * System.out.println("Updated " + x + " games."); return x > 0; }
	 */

	/**
	 * Update the player using the details provided. The player to be updated is
	 * specified using the rowId, and it is altered to use the data passed in
	 * 
	 * @param rowId
	 *            id of player to update
	 * @param name
	 *            the name of the player
	 * @param team
	 *            the team of the player
	 * @param position
	 *            the position of the player (empty string if not applicable)
	 * @param number
	 *            the number of the player (empty string if not applicable)
	 * @return true if the player was successfully updated, false otherwise
	 */
	/*
	 * public boolean updatePlayer(long rowId, String name, String team, String
	 * position, String number) { ContentValues newValues = new ContentValues();
	 * newValues.put(myContext.getString(R.string.KEY_PLAYERS_NAME), name);
	 * newValues.put(myContext.getString(R.string.KEY_PLAYERS_TEAM), team);
	 * newValues.put(myContext.getString(R.string.KEY_PLAYERS_POSITION),
	 * position);
	 * newValues.put(myContext.getString(R.string.KEY_PLAYERS_NUMBER), number);
	 * System.out.println("Updating player at row " + rowId + "..."); int x =
	 * myDatabase.update(myContext .getString(R.string.DATABASE_TABLE_PLAYERS),
	 * newValues, myContext.getString(R.string.KEY_PLAYERS_ROWID) + "=" + rowId,
	 * null); System.out.println("Updated " + x + " players."); return x > 0; }
	 */

	/**
	 * Update the team using the details provided. The team to be updated is
	 * specified using the rowId, and it is altered to use the data passed in
	 * 
	 * @param rowId
	 *            id of team to update
	 * @param school
	 *            the school name of the team (Vanderbilt, Kentucky, etc)
	 * @param name
	 *            the name of the team (Commodores, Wildcats, etc)
	 * @param conference
	 *            the conference of the team (SEC, Big 10, etc)
	 * @return true if the team was successfully updated, false otherwise
	 */
	/*
	 * public boolean updateTeam(long rowId, String school, String name, String
	 * conference) { ContentValues newValues = new ContentValues();
	 * newValues.put(myContext.getString(R.string.KEY_TEAMS_SCHOOL), school);
	 * newValues.put(myContext.getString(R.string.KEY_TEAMS_NAME), name);
	 * newValues.put(myContext.getString(R.string.KEY_TEAMS_CONFERENCE),
	 * conference); System.out.println("Updating player at row " + rowId +
	 * "..."); int x = myDatabase.update(myContext
	 * .getString(R.string.DATABASE_TABLE_TEAMS), newValues, myContext
	 * .getString(R.string.KEY_TEAMS_ROWID) + "=" + rowId, null);
	 * System.out.println("Updated " + x + " teams."); return x > 0; }
	 */
}