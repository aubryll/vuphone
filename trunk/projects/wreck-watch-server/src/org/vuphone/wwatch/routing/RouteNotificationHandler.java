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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class RouteNotificationHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
	.getLogger(RouteNotificationHandler.class.getName());
	
	private DataSource ds_;

	public Notification handle(Notification n) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		
		try {
			RouteNotification rn = (RouteNotification)n;
			Connection db = null;

			try {
				db = ds_.getConnection();
				db.setAutoCommit(true);
			} catch (SQLException e) {
				closeDatabase(db);
				logger_.log(Level.SEVERE,
						"SQLException: ", e);
			}
			if (db != null){
				String sql;

				int id = 0;
				try {
					PreparedStatement prep = db.prepareStatement("select id from People where AndroidID like ?;");
					prep.setString(1, rn.getPerson());

					ResultSet rs  = prep.executeQuery();

					try{
						rs.next();
						id = rs.getInt("id");
						rs.close();
					}catch (SQLException e) {
						//Wait for a few to see if the notification comes in
						Thread.sleep(5000);
						prep = db.prepareStatement("select id from People where AndroidID like ?;");
						prep.setString(1, rn.getPerson());

						rs  = prep.executeQuery();

						try{
							rs.next();
							id = rs.getInt("id");
							rs.close();
						}catch(SQLException sqle){
							closeDatabase(db);
							return null;
						}
						

					}

					prep = db.prepareStatement("select max(wreckid) from Wreck where Person = ?");
					prep.setInt(1, id);
					rs = prep.executeQuery();
					int wid;
					try{
						rs.next();
						wid = rs.getInt("max(wreckid)");
						rs.close();
					}catch (SQLException e) {
						//No wreck exists, we can disregard because there's no accident that's been
						//reported anyway!
						e.printStackTrace();
						closeDatabase(db);
						return null;

					}

					db.setAutoCommit(false);

					sql = "insert into route(wreckid, lat, lon, time) values (?, ?, ?, ?);";
					prep = db.prepareStatement(sql);
					Route route = rn.getRoute();
					while (route.peek() != null){
						Waypoint temp = route.getNextPoint();
						prep.setInt(1, wid);
						prep.setDouble(2, temp.getLatitude());
						prep.setDouble(3, temp.getLongitude());
						prep.setLong(4, temp.getTime());
						prep.addBatch();
					}

					prep.executeBatch();
					db.commit();
					closeDatabase(db);
					return new RouteHandledNotification();
				}catch (SQLException e) {
					logger_.log(Level.SEVERE,
							"SQLException: ", e);
					closeDatabase(db);
					return null;
				}

			
			}
		}catch (Exception sqle) {
			
			return null;
		}
		return null;
		
	}

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "Failed to close database: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	public DataSource getDataConnection(){
		return ds_;
	}


}
