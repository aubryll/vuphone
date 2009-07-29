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
	 * @param ep
	 * @param locationId
	 * @return
	 * @throws SQLException
	 */
	private int createEvent(EventPost ep, int locationId) throws SQLException{
		Connection conn = ds_.getConnection();
		String sql = "insert into events (name, locationid, userid, starttime, endtime, lastupdate)" +
				" values (?, ?, ?, ?, ?, ?)";
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, ep.getName());
		prep.setInt(2, locationId);
		prep.setLong(3, ep.getDbUserId());
		prep.setLong(4, ep.getStartTime());
		prep.setLong(5, ep.getEndTime());
		prep.setLong(6, System.currentTimeMillis());

		if (prep.executeUpdate() == 0){
			throw new SQLException("Insertion into vandyupon.events failed for an unknown reason");
		}else{
			//Everything worked
			ResultSet rs = prep.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);

			sql = "insert into eventmeta (eventid, value, submissiontime, metatype) " +
					"values (?, ?, ?, (select typeid from metatypes where typename like 'DESCRIPTION'))";

			prep = conn.prepareStatement(sql);
			prep.setInt(1, id);
			prep.setString(2, ep.getDescription());
			prep.setLong(3, System.currentTimeMillis());

			prep.execute();
			return id;
		}


	}


	public ResponseNotification handle(Notification n) throws HandlerFailedException {

		if (!(n instanceof EventPost)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}

		EventPost ep = (EventPost) n;
		try{
			if (verifyUserID(ep)){
				int locationid = getLocationId(ep);
				return new EventPostResponse(createEvent(ep, locationid), 
						ep.getResponseType(), ep.getCallback());
			}
		}catch (SQLException e){
			e.printStackTrace();
		}

		return null;
	}

	public DataSource getDataConnection(){
		return ds_;
	}

	/**
	 * Private helper method that accesses the database to return the id of the location
	 * at a given point.  If no location is currently at that point a new one is created.
	 * @return
	 */
	private int getLocationId(EventPost ep) throws SQLException{
		String sql;
		Connection conn;
		conn = ds_.getConnection();
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
		}catch(SQLException e){
			sql = "insert into locations (name, lat, lon, date, userid, lastupdate) " +
					"values (?, ?, ?, ?, ?, ?)";
			prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, "unknown");
			prep.setDouble(2, ep.getLocation().getLat());
			prep.setDouble(3, ep.getLocation().getLon());
			prep.setLong(4, System.currentTimeMillis());
			prep.setLong(5, ep.getDbUserId());
			prep.setLong(6, System.currentTimeMillis());

			prep.execute();
			rs = prep.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		}


		return id;

	}

	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}

	/**
	 * This helper method is designed to check the validity of the user id submitted with the
	 * event.
	 * @param ep - The event post object
	 * @return boolean - Whether the id is a valid user or not
	 * @throws SQLException
	 */
	private boolean verifyUserID(EventPost ep) throws SQLException{
		String sql;
		Connection conn = ds_.getConnection();
		sql = "select * from people where deviceid like ?";
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, ep.getUser());
		ResultSet rs = prep.executeQuery();
		try{
			rs.next();
			int id = rs.getInt(1);
			if (id != 0){
				ep.setDbUserId(id);
				rs.close();
				return true;
			}else{
				rs.close();
				return false;
			}
		}catch (SQLException e){
			sql = "insert into people (deviceid) values (?)";
			prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, ep.getUser());

			if (prep.executeUpdate() != 0){
				ResultSet rs2 = prep.getGeneratedKeys();
				rs2.next();
				ep.setDbUserId(rs2.getInt(1));
				return true;
			}else {
				return false;
			}

		}

	}




}
