/**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.routing;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class RouteHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(RouteHandler.class
			.getName());

	// XML will instantiate these
	private RouteParser parser_;
	private DataSource ds_;

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	public Notification handle(Notification n) {

		// Sleep for 10 seconds to make sure that the posting of the wreck has
		// finished
		// This is a hack because SQLite will throw an exception if the database
		// is currently being written to
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Connection db = null;

		try {
			db = ds_.getConnection();
			db.setAutoCommit(true);
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "SQLException connecting to database: "
					+ e.getMessage());
			return n;
		}

		RouteNotification rn = null;
		try {
			rn = parser_.getRoute(n.getRequest());
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to parse the notification, stopping");
			closeDatabase(db);
			return n;
		}

		String sql;

		int id = 0;
		try {
			PreparedStatement prep = db
					.prepareStatement("select id from People where AndroidID like ?;");
			prep.setString(1, rn.getPerson());

			ResultSet rs = prep.executeQuery();

			try {
				rs.next();
				id = rs.getInt("id");
				rs.close();
			} catch (SQLException e) {
				closeDatabase(db);
				return n;
			}

			prep = db
					.prepareStatement("select max(wreckid) from Wreck where Person = ?");
			prep.setInt(1, id);
			rs = prep.executeQuery();
			int wid;
			try {
				rs.next();
				wid = rs.getInt("max(wreckid)");
				rs.close();
			} catch (SQLException e) {
				// No wreck exists, we can disregard because there's no
				// accident that's been
				// reported anyway!
				e.printStackTrace();
				closeDatabase(db);
				return n;

			}

			db.setAutoCommit(false);

			sql = "insert into route(wreckid, lat, lon, date, time) values (?, ?, ?, ?, ?);";
			prep = db.prepareStatement(sql);
			Route route = rn.getRoute();
			while (route.peek() != null) {
				Waypoint temp = route.getNextPoint();
				prep.setInt(1, wid);
				prep.setDouble(2, temp.getLatitude());
				prep.setDouble(3, temp.getLongitude());
				prep.setDate(4, new Date(temp.getTime()));
				prep.setTime(5, new Time(temp.getTime()));
				prep.addBatch();
			}

			prep.executeBatch();
			db.commit();
			closeDatabase(db);
			return rn;

		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "SQLException: ", e);
			closeDatabase(db);
			return n;
		}

	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	public DataSource getDataConnection() {
		return ds_;
	}
	
	public void setParser(RouteParser p) {
		parser_ = p;
	}
	
	public RouteParser getParser() {
		return parser_;
	}

}
