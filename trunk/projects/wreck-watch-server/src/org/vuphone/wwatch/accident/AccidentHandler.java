/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.accident;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vuphone.wwatch.asterisk.AsteriskConnector;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;
import org.vuphone.wwatch.notification.SMSSender;

/**
 * This class is responsible for receiving accident reports and handling the
 * accident response process.
 * 
 * @author jules
 * 
 */
public class AccidentHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
			.getLogger(AccidentHandler.class.getName());

	private AccidentParser parser_ = new AccidentParser();;

	/**
	 * This method receives a notification of an accident, converts the
	 * notification into an AccidentReport, and then determines the proper way
	 * to respond to the accident. This is typically by getting the emergency
	 * contacts, and notifying them, either by SMS, voice messagee, or both
	 * 
	 * @see org.vuphone.wwatch.notification.NotificationHandler#handle(Notification)
	 */
	public Notification handle(Notification n) {

		// Initialize database driver
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"AccidentHandler was unable to initialize org.sqlite.JDBC");
			logger_.log(Level.SEVERE,
					" Unable to continue without a database, stopping");
			return n;
		}

		// Parse the Notification and extract the AccidentReport
		AccidentReport report;
		try {
			report = parser_.getAccident(n);
		} catch (AccidentFormatException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE,
					"AccidentHandler was unable to parse the Notification:");
			logger_.log(Level.SEVERE, " Notification was: ");
			logger_.log(Level.SEVERE, " " + n.toString());
			logger_.log(Level.SEVERE,
					" Unable to continue without AccidentReport, stopping");
			return n;
		}

		// Fetch the database
		Connection db = null;
		try {
			db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");
			db.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			logger_
					.log(Level.SEVERE,
							"AccidentHandler was unable to connect to wreckwatch.db database");
			logger_.log(Level.SEVERE, "SQLException: ", e);
			logger_.log(Level.SEVERE,
					" Unable to continue without database, stopping");
			return n;
		}

		// Check its validity
		if (db == null) {
			logger_
					.log(
							Level.SEVERE,
							"AccidentHandler fetched database with no exceptions, but the returned database was NULL");
			logger_.log(Level.SEVERE,
					" Unable to continue without database, stopping");
			return n;
		}

		// Used as the row ID of the person who wrecked
		int id = 0;

		// Prepare the SQL statement
		PreparedStatement prep = null;
		try {
			prep = db
					.prepareStatement("SELECT id FROM People WHERE AndroidID LIKE ?;");
			prep.setString(1, report.getParty());
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_
					.log(
							Level.SEVERE,
							"AccidentHandle was unable to prepare a SQL statement to return the Contact ID based off the Android ID");
			logger_.log(Level.SEVERE,
					" Unable to continue without statement, stopping");
			return n;
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
			}
		}

		// Execute SQL statement
		ResultSet rs;
		try {
			rs = prep.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_
					.log(
							Level.SEVERE,
							"AccidentHandler was unable to execute a SQL statement to return the Contact ID based off the Android ID");
			logger_.log(Level.SEVERE,
					" Unable to continue witout ResultSet, stopping");
			return n;
		}

		// Access results of SQL statement (Creating person if they do not
		// exist)
		try {
			// Check that they do exist, try to create them if they do not
			boolean doesPersonExist = rs.first();
			if (doesPersonExist == false) {
				if (insertPerson(db, report) == false) {
					logger_
							.log(Level.SEVERE,
									"AccidentHanlder (possibly) unable to insert a new person");
					logger_
							.log(Level.SEVERE,
									" Continuing in the hope that it was added, we will fail ");
					logger_.log(Level.SEVERE,
							" shortly if it was indeed not inserted");
				}
			}

			// get the ID of the created person
			prep = db
					.prepareStatement("SELECT id FROM People WHERE AndroidID LIKE ?;");
			prep.setString(1, report.getParty());

			rs = prep.executeQuery();
			rs.first();
			id = rs.getInt("id");
			rs.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_
					.log(Level.SEVERE,
							"AccidentHandler unable to get the ID of the last inserted person");
			logger_
					.log(
							Level.SEVERE,
							" Either the person does not exist (was not inserted), or the ResultSet went bad");
			logger_.log(Level.SEVERE,
					" Unable to continue with either error, stopping");
			return n;
		}

		// Enable finer-grained control of database
		try {
			db.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			logger_
					.log(Level.WARNING,
							"AccidentHandler was unable to set database autocommit to false");
			logger_
					.log(Level.WARNING,
							" This may indicate a problem, but program execution will not stop");
		}

		// Insert wreck into database
		try {
			prep = db
					.prepareStatement("INSERT INTO Wreck (Person, Lat, Lon, Time, LargestAccel) VALUES (?, ?, ?, ?,?);");
			prep.setInt(1, id);
			prep.setDouble(2, report.getLatitude());
			prep.setDouble(3, report.getLongitude());
			prep.setDate(4, new Date(report.getTime()));
			prep.setDouble(5, report.getAcceleration());
			prep.addBatch();
			prep.executeBatch();

			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Accidenthandler unable to insert wreck into database!");
			logger_.log(Level.SEVERE,
					" This is very bad, core functionality is broken");
			logger_.log(Level.SEVERE, " AccidentReport: ");
			logger_.log(Level.SEVERE, " " + report.toString());
			logger_
					.log(Level.SEVERE,
							" Continuing execution, attempting to contact emergency contacts");
		}

		// Retrieve emergency contact numbers
		ArrayList<String> nums = new ArrayList<String>();
		try {
			prep = db
					.prepareStatement("SELECT ContactId FROM EmergencyContacts WHERE PersonId = ?");
			prep.setInt(1, id);
			rs = prep.executeQuery();
			while (rs.next()) {
				nums.add(rs.getString("ContactId"));
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_.log(Level.WARNING,
					"AccidentHandler unable to retrieve emergency contacts");
			logger_
					.log(Level.WARNING,
							" Not absolutely terrible, but this should definitely be checked out");
			logger_
					.log(Level.WARNING,
							" Continuing execution to close DB and return Notification");
		}

		// Try to force the DB closed
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING,
					"AccidentHandler was unable to close database");
			logger_.log(Level.WARNING,
					" Not a problem, it should be cleaned up automatically");
		}

		// Contact emergency contacts
		for (String s : nums) {
			SMSSender.sendText(s);
			AsteriskConnector.playRecordingToPSTNNumber(s);
		}

		// Create reply indicating we have handled the accident
		return new AccidentHandledNotification();
	}

	/**
	 * Try to create a user when they do not exist (in the database ;) )
	 * 
	 * @param db
	 *            the opened, and ready to go, database connection
	 * @param report
	 *            the report containing the users information
	 * @return -1 if the person was not inserted, or the rowID of the person if
	 *         they were inserted
	 */
	private boolean insertPerson(Connection db, AccidentReport report) {

		PreparedStatement prep = null;
		try {
			prep = db
					.prepareStatement("INSERT INTO People (AndroidID) VALUES (?)");
			prep.setString(1, report.getParty());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_
					.log(Level.SEVERE,
							"AccidentHandler unable to insert new person into people table");
			return false;
		}

		return true;
	}

	public AccidentParser getParser() {
		return parser_;
	}

	public void setParser(AccidentParser parser) {
		parser_ = parser;
	}

}