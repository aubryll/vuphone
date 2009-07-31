/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.landminerequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.assassins.notification.InvalidFormatException;
import org.vuphone.assassins.notification.Notification;
import org.vuphone.assassins.notification.NotificationHandler;

public class LandMineRequestHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
			.getLogger(LandMineRequestHandler.class.getName());
	
	// XML will instantiate these
	private LandMineRequestParser parser_;
	private DataSource ds_;

	/**
	 * This method receives a notification, tries to convert the
	 * notification into a LandMineNotification, and enters the land
	 * mine in the database.
	 * 
	 * @see org.vuphone.assassins.notification.NotificationHandler#handle(Notification)
	 */
	public Notification handle(Notification n) {

		// Parse the Notification and extract the LandMineNotification
		LandMineRequestNotification report;
		Connection db = null;
		try {
			report = parser_.getLandMine(n.getRequest());
			db = ds_.getConnection();
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE, "Unable to parse the Notification:");
			logger_.log(Level.SEVERE, " Notification was: ");
			logger_.log(Level.SEVERE, " " + n.toString());
			logger_.log(Level.SEVERE, 
					" Unable to continue without LandMineNotification," +
					" stopping");
			closeDatabase(db);
			return n;
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to connect to assassins.db database");

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

		ArrayList<LandMine> mines = new ArrayList<LandMine>();

		// Prepare the SQL statement
		PreparedStatement prep = null;
		ResultSet rs;

		try {
			prep = db.prepareStatement("SELECT * FROM LandMine;");

			rs = prep.executeQuery();
			while (rs.next()) {
				mines.add(new LandMine(rs.getDouble("Lat"), 
						rs.getDouble("Lon"), rs.getDouble("Radius")));
			}
			rs.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to get the list of current land mines");

			logger_.log(Level.SEVERE,
							" Either there are no land mines," +
							" or the ResultSet went bad");
			logger_.log(Level.SEVERE,
					" Unable to continue with either error, stopping");
			closeDatabase(db);
			return n;
		}

		
		closeDatabase(db);

		
		// Return a LandMineListNotification
		return createXML(mines, report);
	}
	
	private LandMineRequestNotification createXML(ArrayList<LandMine> mines, 
			LandMineRequestNotification lmr) {

		StringBuilder sb = new StringBuilder();
		if (mines.size() > 0) {
			sb.append("<LandMines>");
			for (int i = 0; i < mines.size(); i++) {
				LandMine m = mines.get(i);
				sb.append("<LandMine><Lat>"+m.getLatitude()+"</Lat><Lon>"
						+m.getLongitude()+"</Lon><Radius>"+m.getRadius()
						+"</Radius></LandMine>");
			}
			sb.append("</LandMines>");
		}
		else {
			sb.append("<LandMines/>");
		}
		lmr.setResponseString(sb.toString());
		return lmr;
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
	private boolean insertPerson(LandMineRequestNotification report) {

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
			//prep.setString(1, report.getPerson());
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

	public LandMineRequestParser getParser() {
		return parser_;
	}

	public void setParser(LandMineRequestParser parser) {
		parser_ = parser;
	}


}
