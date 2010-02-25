package edu.vanderbilt.vuphone.android.athletics.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.vanderbilt.vuphone.android.athletics.R;

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
	private static final int DATABASE_VERSION = 8;

	// constant for the context (for the SQLiteOpenHelper)
	private final Context myContext;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		Context context;

		DatabaseHelper(Context context) {
			super(context, context.getString(R.string.DATABASE_NAME), null,
					DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(context.getString(R.string.DATABASE_CREATE1));
			db.execSQL(context.getString(R.string.DATABASE_CREATE2));
			db.execSQL(context.getString(R.string.DATABASE_CREATE3));
			db.execSQL(context.getString(R.string.DATABASE_CREATE4));
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(context.getString(R.string.DATABASE_TAG),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS "
					+ context.getString(R.string.DATABASE_TABLE_NEWS));
			db.execSQL("DROP TABLE IF EXISTS "
					+ context.getString(R.string.DATABASE_TABLE_GAMES));
			db.execSQL("DROP TABLE IF EXISTS "
					+ context.getString(R.string.DATABASE_TABLE_PLAYERS));
			db.execSQL("DROP TABLE IF EXISTS "
					+ context.getString(R.string.DATABASE_TABLE_TEAMS));
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
	public long createNewsItem(String title, String body, String link) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(myContext.getString(R.string.KEY_NEWS_TITLE), title);
		initialValues.put(myContext.getString(R.string.KEY_NEWS_BODY), body);
		initialValues.put(myContext.getString(R.string.KEY_NEWS_LINK), link);
		long x = myDatabase.insert(myContext
				.getString(R.string.DATABASE_TABLE_NEWS), null, initialValues);
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
	public long createGame(String hometeam, String awayteam, String sport,
			String type, String time, String homescore, String awayscore) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(myContext.getString(R.string.KEY_GAMES_HOMETEAM),
				hometeam);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_AWAYTEAM),
				awayteam);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_SPORT), sport);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_TYPE), type);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_TIME), time);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_HOMESCORE),
				homescore);
		initialValues.put(myContext.getString(R.string.KEY_GAMES_AWAYSCORE),
				awayscore);

		System.out.println("Creating game: " + awayteam + " @ " + hometeam
				+ "...");
		long x = myDatabase.insert(myContext
				.getString(R.string.DATABASE_TABLE_GAMES), null, initialValues);
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
	public long createPlayer(String name, String team, String position,
			String number) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(myContext.getString(R.string.KEY_PLAYERS_NAME), name);
		initialValues.put(myContext.getString(R.string.KEY_PLAYERS_TEAM), team);
		initialValues.put(myContext.getString(R.string.KEY_PLAYERS_POSITION),
				position);
		initialValues.put(myContext.getString(R.string.KEY_PLAYERS_NUMBER),
				number);

		System.out.println("Creating player: " + name + " on the " + team
				+ "...");
		long x = myDatabase.insert(myContext
				.getString(R.string.DATABASE_TABLE_PLAYERS), null,
				initialValues);
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
	public long createTeam(String school, String name, String conference) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(myContext.getString(R.string.KEY_TEAMS_SCHOOL),
				school);
		initialValues.put(myContext.getString(R.string.KEY_TEAMS_NAME), name);
		initialValues.put(myContext.getString(R.string.KEY_TEAMS_CONFERENCE),
				conference);

		System.out.println("Creating team: " + school + " " + name + "...");
		long x = myDatabase.insert(myContext
				.getString(R.string.DATABASE_TABLE_TEAMS), null, initialValues);
		if (x == -1) {
			System.out.println("TEAM CREATION FAILED");
		} else {
			System.out.println("Team created at row: " + x);
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
	public boolean deleteNewsItem(long rowId) {
		System.out.println("Deleting news item at row" + rowId + "...");
		int x = myDatabase.delete(myContext
				.getString(R.string.DATABASE_TABLE_NEWS), myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null);
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
	public boolean deleteGame(long rowId) {
		System.out.println("Deleting game at row" + rowId + "...");
		int x = myDatabase.delete(myContext
				.getString(R.string.DATABASE_TABLE_GAMES), myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null);
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
	public boolean deletePlayer(long rowId) {
		System.out.println("Deleting player at row" + rowId + "...");
		int x = myDatabase.delete(myContext
				.getString(R.string.DATABASE_TABLE_PLAYERS), myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null);
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
	public boolean deleteTeam(long rowId) {
		System.out.println("Deleting team at row" + rowId + "...");
		int x = myDatabase.delete(myContext
				.getString(R.string.DATABASE_TABLE_TEAMS), myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null);
		System.out.println("Deleted " + x + " team(s).");
		return x > 0;
	}

	/**
	 * Return a Cursor over the list of all news items in the database
	 * 
	 * @return Cursor over all news items
	 */
	public Cursor fetchAllNewsItems() {

		System.out.println("Fetching all news items...");
		return myDatabase.query(myContext
				.getString(R.string.DATABASE_TABLE_NEWS), null, null, null,
				null, null, null);
	}

	/**
	 * Return a Cursor over the list of all games in the database
	 * 
	 * @return Cursor over all games
	 */
	public Cursor fetchAllGames() {

		System.out.println("Fetching all games...");
		return myDatabase.query(myContext
				.getString(R.string.DATABASE_TABLE_GAMES), null, null, null,
				null, null, null);
	}

	/**
	 * Return a Cursor over the list of all players in the database
	 * 
	 * @return Cursor over all players
	 */
	public Cursor fetchAllPlayers() {

		System.out.println("Fetching all players...");
		return myDatabase.query(myContext
				.getString(R.string.DATABASE_TABLE_PLAYERS), null, null, null,
				null, null, null);
	}

	/**
	 * Return a Cursor over the list of all teams in the database
	 * 
	 * @return Cursor over all teams
	 */
	public Cursor fetchAllTeams() {

		System.out.println("Fetching all teams...");
		return myDatabase.query(myContext
				.getString(R.string.DATABASE_TABLE_TEAMS), null, null, null,
				null, null, null);
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
	public Cursor fetchNewsItem(long rowId) throws SQLException {
		System.out.println("Fetching news item at row" + rowId + "...");
		Cursor myCursor = myDatabase.query(true, myContext
				.getString(R.string.DATABASE_TABLE_NEWS), null, myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null, null, null, null, null);
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
	public Cursor fetchGame(long rowId) throws SQLException {
		System.out.println("Fetching game at row" + rowId + "...");
		Cursor myCursor = myDatabase.query(true, myContext
				.getString(R.string.DATABASE_TABLE_GAMES), null, myContext
				.getString(R.string.KEY_GAMES_ROWID)
				+ "=" + rowId, null, null, null, null, null);
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
	public Cursor fetchPlayer(long rowId) throws SQLException {
		System.out.println("Fetching player at row" + rowId + "...");

		Cursor myCursor = myDatabase.query(true, myContext
				.getString(R.string.DATABASE_TABLE_PLAYERS), null, myContext
				.getString(R.string.KEY_PLAYERS_ROWID)
				+ "=" + rowId, null, null, null, null, null);
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
	public Cursor fetchTeam(long rowId) throws SQLException {
		System.out.println("Fetching team at row" + rowId + "...");

		Cursor myCursor = myDatabase.query(true, myContext
				.getString(R.string.DATABASE_TABLE_TEAMS), null, myContext
				.getString(R.string.KEY_TEAMS_ROWID)
				+ "=" + rowId, null, null, null, null, null);
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
	public boolean updateNewsItem(long rowId, String title, String body,
			String link) {
		ContentValues newValues = new ContentValues();
		newValues.put(myContext.getString(R.string.KEY_NEWS_TITLE), title);
		newValues.put(myContext.getString(R.string.KEY_NEWS_BODY), body);
		newValues.put(myContext.getString(R.string.KEY_NEWS_LINK), link);
		System.out.println("Updating news item at row " + rowId + "...");
		int x = myDatabase.update(myContext
				.getString(R.string.DATABASE_TABLE_NEWS), newValues, myContext
				.getString(R.string.KEY_NEWS_ROWID)
				+ "=" + rowId, null);
		System.out.println("Updated " + x + " news items.");
		return x > 0;
	}

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
	public boolean updateGame(long rowId, String hometeam, String awayteam,
			String sport, String type, String time, String homescore,
			String awayscore) {
		ContentValues newValues = new ContentValues();
		newValues.put(myContext.getString(R.string.KEY_GAMES_HOMETEAM),
				hometeam);
		newValues.put(myContext.getString(R.string.KEY_GAMES_AWAYTEAM),
				awayteam);
		newValues.put(myContext.getString(R.string.KEY_GAMES_SPORT), sport);
		newValues.put(myContext.getString(R.string.KEY_GAMES_TYPE), type);
		newValues.put(myContext.getString(R.string.KEY_GAMES_TIME), time);
		newValues.put(myContext.getString(R.string.KEY_GAMES_HOMESCORE),
				homescore);
		newValues.put(myContext.getString(R.string.KEY_GAMES_AWAYSCORE),
				awayscore);
		System.out.println("Updating game at row " + rowId + "...");
		int x = myDatabase.update(myContext
				.getString(R.string.DATABASE_TABLE_GAMES), newValues, myContext
				.getString(R.string.KEY_GAMES_ROWID)
				+ "=" + rowId, null);
		System.out.println("Updated " + x + " games.");
		return x > 0;
	}

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
	public boolean updatePlayer(long rowId, String name, String team,
			String position, String number) {
		ContentValues newValues = new ContentValues();
		newValues.put(myContext.getString(R.string.KEY_PLAYERS_NAME), name);
		newValues.put(myContext.getString(R.string.KEY_PLAYERS_TEAM), team);
		newValues.put(myContext.getString(R.string.KEY_PLAYERS_POSITION),
				position);
		newValues.put(myContext.getString(R.string.KEY_PLAYERS_NUMBER), number);
		System.out.println("Updating player at row " + rowId + "...");
		int x = myDatabase.update(myContext
				.getString(R.string.DATABASE_TABLE_PLAYERS), newValues,
				myContext.getString(R.string.KEY_PLAYERS_ROWID) + "=" + rowId,
				null);
		System.out.println("Updated " + x + " players.");
		return x > 0;
	}

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
	public boolean updateTeam(long rowId, String school, String name,
			String conference) {
		ContentValues newValues = new ContentValues();
		newValues.put(myContext.getString(R.string.KEY_TEAMS_SCHOOL), school);
		newValues.put(myContext.getString(R.string.KEY_TEAMS_NAME), name);
		newValues.put(myContext.getString(R.string.KEY_TEAMS_CONFERENCE),
				conference);
		System.out.println("Updating player at row " + rowId + "...");
		int x = myDatabase.update(myContext
				.getString(R.string.DATABASE_TABLE_TEAMS), newValues, myContext
				.getString(R.string.KEY_TEAMS_ROWID)
				+ "=" + rowId, null);
		System.out.println("Updated " + x + " teams.");
		return x > 0;
	}
}