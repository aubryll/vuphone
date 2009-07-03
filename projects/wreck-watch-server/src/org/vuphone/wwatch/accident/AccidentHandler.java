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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.asterisk.AsteriskConnector;
import org.vuphone.wwatch.ebehavior.Behavior;
import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

/**
 * This class is responsible for receiving accident reports and handling the
 * accident response process.
 * 
 * @author jules
 * 4
 */
public class AccidentHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
			.getLogger(AccidentHandler.class.getName());
	
	private static final boolean CALL_ASTERISK = true;
	
	// XML will instantiate these
	private AccidentParser parser_;
	private DataSource ds_;
	private Behavior b_ = null;

	/**
	 * This method receives a notification, tries to convert the
	 * notification into an AccidentNotification, and then determines the proper way
	 * to respond to the accident. This is typically by getting the emergency
	 * contacts, and notifying them, either by SMS, voice message, or both
	 * 
	 * @see org.vuphone.wwatch.notification.NotificationHandler#handle(Notification)
	 */
	public Notification handle(Notification n) {

		// Parse the Notification and extract the AccidentNotification
		AccidentNotification report;
		Connection db = null;
		try {
			report = parser_.getAccident(n.getRequest());
			db = ds_.getConnection();
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE, "Unable to parse the Notification:");
			logger_.log(Level.SEVERE, " Notification was: ");
			logger_.log(Level.SEVERE, " " + n.toString());
			logger_
					.log(Level.SEVERE,
							" Unable to continue without AccidentNotification, stopping");
			closeDatabase(db);
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to connect to wreckwatch.db database");

			logger_.log(Level.SEVERE, "SQLException: ", e);
			logger_.log(Level.SEVERE,
					" Unable to continue without database, stopping");
			closeDatabase(db);
			return n;
		}

		// Enable fine grained control of the database
		try {
			db.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING,
					"Unable to control database at a fine-grained level");
		}

		// Used as the row ID of the person who wrecked
		int id = 0;

		// Prepare the SQL statement
		PreparedStatement prep = null;
		ResultSet rs;
		try {
			prep = db
					.prepareStatement("SELECT id FROM people WHERE AndroidID LIKE ?;");
			prep.setString(1, report.getPerson());
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_
					.log(
							Level.SEVERE,
							"Unable to prepare a SQL statement to return the Contact ID based off the Android ID");
			logger_.log(Level.SEVERE,
					" Unable to continue without statement, stopping");
			closeDatabase(db);
			return n;
		}

		// Execute SQL statement
		try {
			rs = prep.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_
					.log(
							Level.SEVERE,
							"Unable to execute a SQL statement to return the Contact ID based off the Android ID");

			logger_.log(Level.SEVERE,
					" Unable to continue without statement, stopping");
			closeDatabase(db);
			return n;
		}

		// Access results of SQL statement (Creating person if they do not
		// exist)
		try {
			rs.next();
			id = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			if (insertPerson(report) == false) {
				logger_.log(Level.SEVERE,
						"(possibly) Unable to insert a new person");
				logger_
						.log(
								Level.SEVERE,
								" Continuing in the hope that it was added, we will"
										+ " fail shortly if it was indeed not inserted");
			}
		}

		// get the ID of the created person
		try {
			prep = db
					.prepareStatement("SELECT id FROM people WHERE AndroidID LIKE ?;");
			prep.setString(1, report.getPerson());

			rs = prep.executeQuery();
			rs.next();
			id = rs.getInt("id");
			rs.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to get the ID of the last inserted person");

			logger_
					.log(
							Level.SEVERE,
							" Either the person does not exist (was not inserted), or the ResultSet went bad");
			logger_.log(Level.SEVERE,
					" Unable to continue with either error, stopping");
			closeDatabase(db);
			return n;
		}

		// Insert wreck into database
		try {
			prep = db
					.prepareStatement("INSERT INTO wreck (Person, Lat, Lon, Date, LargestAccel) VALUES (?, ?, ?, ?, ?);");
			prep.setInt(1, id);
			prep.setDouble(2, report.getLatitude());
			prep.setDouble(3, report.getLongitude());
			prep.setLong(4, report.getTime());
			prep.setDouble(5, report.getDeceleration());
			prep.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE, "Unable to insert wreck into database!");

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
					.prepareStatement("SELECT ContactId FROM emergencycontacts WHERE PersonId = ?");
			prep.setInt(1, id);
			rs = prep.executeQuery();
			while (rs.next()) {
				nums.add(rs.getString("ContactId"));
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_.log(Level.WARNING, "Unable to retrieve emergency contacts");

			logger_
					.log(Level.WARNING,
							" Not absolutely terrible, but this should definitely be checked out");
			logger_
					.log(Level.WARNING,
							" Continuing execution to close DB and return Notification");
		}
		closeDatabase(db);

		// Contact emergency contacts
		b_.execute(nums);
		
		// Call a default 911 Emergency number
		
		String emergency = "311";
		String recording = "zip-code";
		if (CALL_ASTERISK)
			AsteriskConnector.makeCallPlayRecording(emergency, recording);
		
		// Return an AccidentNotification
		return report;
	}

	/**
	 * Try to create a user when they do not exist (in the database ;) )
	 * 
	 * @param db_
	 *            the opened, and ready to go, database connection
	 * @param report
	 *            the report containing the users information
	 * @return -1 if the person was not inserted, or the rowID of the person if
	 *         they were inserted
	 */
	private boolean insertPerson(AccidentNotification report) {

		Connection db;
		try {
			db = ds_.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to get database connection for insertion");
			logger_.log(Level.SEVERE,
					" unable to continue without database, stopping");
			return false;
		}

		PreparedStatement prep = null;

		try {
			prep = db
					.prepareStatement("INSERT INTO people (AndroidID) VALUES (?)");
			prep.setString(1, report.getPerson());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to insert new person into people table");
			return false;
		}

		return true;
	}

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

	public AccidentParser getParser() {
		return parser_;
	}

	public void setParser(AccidentParser parser) {
		parser_ = parser;
	}

	public void setEBehavior(Behavior b) {
		b_ = b;
	}

	public Behavior getEBehavior() {
		return b_;
	}

}
