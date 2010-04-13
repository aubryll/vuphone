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
package org.vuphone.vandyupon.notification.eventpost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventPostHandler implements NotificationHandler {

	private DataSource ds_;

	/**
	 * Helper method that generates an event entry in the database and returns
	 * the index of the newly created event.
	 * 
	 * @param ep
	 * @param locationId
	 * @return
	 * @throws SQLException
	 */
	private int createEvent(EventPost ep, int locationId) throws SQLException {
		Connection conn = ds_.getConnection();

		// First check if the event already exists
		String sql = "SELECT eventid FROM events WHERE sourceuid = ?";
		PreparedStatement prep = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, ep.getSourceUid());
		ResultSet rs = prep.executeQuery();

		boolean isExisting = false;
		if (rs.next()) {
			isExisting = true;
			// This is an existing event, so update
			sql = "update events set name = ?, locationid = ?, userid = ?, starttime = ?, endtime = ?"
					+ ", lastupdate = ?, sourceuid = ?";
		} else {
			// This is a new event, so insert
			sql = "insert into events (name, locationid, userid, starttime, endtime, lastupdate, sourceuid)"
					+ " values (?, ?, ?, ?, ?, ?, ?)";
		}

		prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, ep.getName());
		prep.setInt(2, locationId);
		prep.setLong(3, ep.getDbUserId());
		prep.setLong(4, ep.getStartTime());
		prep.setLong(5, ep.getEndTime());
		prep.setLong(6, System.currentTimeMillis() / 1000);
		prep.setString(7, ep.getSourceUid());

		if (prep.executeUpdate() == 0) {
			throw new SQLException(
					"Insertion into vandyupon.events failed for an unknown reason");
		} else {
			// Everything worked
			rs = prep.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);

			// Insert Description, if one exists
			if (isExisting && ep.getDescription() != null) {
				sql = "UPDATE eventmeta (eventid, value, submissiontime, metatype) "
						+ "SET value = ?, submissiontime = ? "
						+ "WHERE eventid = ? AND metatype = (select typeid from metatypes where typename like 'DESCRIPTION'))";

				prep = conn.prepareStatement(sql);
				prep.setString(1, ep.getDescription());
				prep.setLong(2, System.currentTimeMillis());
				prep.setInt(3, id);
				prep.execute();
			} else if (ep.getDescription() != null) {
				sql = "insert into eventmeta (eventid, value, submissiontime, metatype) "
						+ "values (?, ?, ?, (select typeid from metatypes where typename like 'DESCRIPTION'))";
				prep = conn.prepareStatement(sql);
				prep.setInt(1, id);
				prep.setString(2, ep.getDescription());
				prep.setLong(3, System.currentTimeMillis());
				prep.execute();
			}

			// Insert Tags, if they exist
			if (isExisting && ep.getTags() != null) {
				for (String s : ep.getTags()) {
					sql = "UPDATE eventmeta (eventid, value, submissiontime, metatype) "

							+ "SET value = ?, submissiontime = ? "
							+ "WHERE eventid = ? AND metatype = (select typeid from metatypes where typename like 'TAG'))";

					prep = conn.prepareStatement(sql);
					prep.setString(1, s);
					prep.setLong(2, System.currentTimeMillis());
					prep.setInt(3, id);
					prep.execute();
				}
			} else if (ep.getTags() != null) {
				for (String s : ep.getTags()) {
					sql = "insert into eventmeta (eventid, value, submissiontime, metatype) "
							+ "values (?, ?, ?, (select typeid from metatypes where typename like 'TAG'))";

					prep = conn.prepareStatement(sql);
					prep.setInt(1, id);
					prep.setString(2, s);
					prep.setLong(3, System.currentTimeMillis());

					prep.execute();
				}
			}

			return id;
		}

	}

	public ResponseNotification handle(Notification n)
			throws HandlerFailedException {

		if (!(n instanceof EventPost)) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}

		EventPost ep = (EventPost) n;
		try {
			if (verifyUserID(ep)) {
				int locationid = getLocationId(ep);
				return new EventPostResponse(createEvent(ep, locationid), ep
						.getResponseType(), ep.getCallback());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

	private static final String NO_NAME = "No Location";
	private int getNoGeoLocationId(EventPost ep) throws SQLException {
		String sql;
		Connection conn;
		PreparedStatement prep;
		conn = ds_.getConnection();
		
		sql = "select * from locations where name LIKE ?";
		prep = conn.prepareStatement(sql);
		if ((ep.getLocationName() == null) || ep.getLocationName().equalsIgnoreCase(""))
			prep.setString(1, NO_NAME);
		else
			prep.setString(1, ep.getLocationName());
		

		int id;
		ResultSet rs = prep.executeQuery();
		rs.next();
		try {
			id = rs.getInt("locationid");
			rs.close();
		} catch (SQLException e) {
			sql = "insert into locations (name, lat, lon, date, userid, lastupdate) "
					+ "values (?, ?, ?, ?, ?, ?)";
			prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			if ((ep.getLocationName() == null) || ep.getLocationName().equalsIgnoreCase(""))
				prep.setString(1, NO_NAME);
			else
				prep.setString(1, ep.getLocationName());

			prep.setNull(2, java.sql.Types.NULL);
			prep.setNull(3, java.sql.Types.NULL);
			prep.setLong(4, System.currentTimeMillis() / 1000);
			prep.setLong(5, ep.getDbUserId());
			prep.setLong(6, System.currentTimeMillis() / 1000);

			prep.execute();

			rs = prep.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		}

		return id;
	}

	/**
	 * Private helper method that accesses the database to return the id of the
	 * location at a given point. If no location is currently at that point a
	 * new one is created.
	 * 
	 * @return
	 */
	private int getLocationId(EventPost ep) throws SQLException {
		String sql;
		Connection conn;
		conn = ds_.getConnection();

		if (ep.getLocation() == null)
			return getNoGeoLocationId(ep);

		sql = "select * from locations where lat = ? and lon = ?";
		PreparedStatement prep = conn.prepareStatement(sql);
		prep.setDouble(1, ep.getLocation().getLat());
		prep.setDouble(2, ep.getLocation().getLon());

		int id;
		ResultSet rs = prep.executeQuery();
		rs.next();
		try {
			id = rs.getInt("locationid");
			rs.close();
		} catch (SQLException e) {
			sql = "insert into locations (name, lat, lon, date, userid, lastupdate) "
					+ "values (?, ?, ?, ?, ?, ?)";
			prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (ep.getLocationName().equalsIgnoreCase(""))
				prep.setNull(1, java.sql.Types.NULL);
			else
				prep.setString(1, ep.getLocationName());
			
			System.out.println("Name is " + ep.getLocationName());
			
			prep.setDouble(2, ep.getLocation().getLat());
			prep.setDouble(3, ep.getLocation().getLon());
			prep.setLong(4, System.currentTimeMillis() / 1000);
			prep.setLong(5, ep.getDbUserId());
			prep.setLong(6, System.currentTimeMillis() / 1000);

			prep.execute();
			rs = prep.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		}

		return id;

	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	/**
	 * This helper method is designed to check the validity of the user id
	 * submitted with the event.
	 * 
	 * @param ep
	 *            - The event post object
	 * @return boolean - Whether the id is a valid user or not
	 * @throws SQLException
	 */
	private boolean verifyUserID(EventPost ep) throws SQLException {
		String sql;
		Connection conn = ds_.getConnection();
		sql = "select * from people where deviceid like ?";
		PreparedStatement prep = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, ep.getUser());
		ResultSet rs = prep.executeQuery();
		try {
			rs.next();
			int id = rs.getInt(1);
			if (id != 0) {
				ep.setDbUserId(id);
				rs.close();
				return true;
			} else {
				rs.close();
				return false;
			}
		} catch (SQLException e) {
			sql = "insert into people (deviceid) values (?)";
			prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, ep.getUser());

			if (prep.executeUpdate() != 0) {
				ResultSet rs2 = prep.getGeneratedKeys();
				rs2.next();
				ep.setDbUserId(rs2.getInt(1));
				return true;
			} else {
				return false;
			}

		}

	}

}
