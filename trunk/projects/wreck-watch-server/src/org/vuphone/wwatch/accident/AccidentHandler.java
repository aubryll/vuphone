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
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;

/**
 * This class is responsible for receiving accident reports
 * and kicking off the accident response process.
 * 
 * @author jules
 *
 */
public class AccidentHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
	.getLogger(AccidentHandler.class.getName());

	private AccidentParser parser_ = new AccidentParser();;

	/**
	 * This method receives a notification of an accident,
	 * converts the notification into an AccidentReport,
	 * and then determines the proper way to respond to the
	 * accident.
	 */
	/* (non-Javadoc)
	 * @see org.vuphone.wwatch.notification.NotificationHandler#handle(org.vuphone.wwatch.notification.Notification)
	 */
	public Notification handle(Notification n) {
		Notification response = null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		try {
			AccidentReport report = parser_.getAccident(n);
			Connection db = null;

			try {
				db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");
				db.setAutoCommit(true);
			} catch (SQLException e) {

				logger_.log(Level.SEVERE,
						"SQLException: ", e);
			}

			if (db != null){
				String sql;

				int id = 0;
				try {
					PreparedStatement prep = db.prepareStatement("select id from People where AndroidID like ?;");
					prep.setString(1, report.getParty());

					ResultSet rs  = prep.executeQuery();

					try{
						rs.next();
						id = rs.getInt("id");
						rs.close();
					}catch (SQLException e) {
						//No user exists, try to create
						prep = db.prepareStatement("insert into People (AndroidID) values (?)");
						prep.setString(1, report.getParty());
						prep.executeUpdate();
						try{
							prep = db.prepareStatement("select id from People where AndroidID like ?;");
							prep.setString(1, report.getParty());

							rs  = prep.executeQuery();
							rs.next();
							id = rs.getInt("id");
							rs.close();
						}catch (Exception ex) {
							//We hosed
							
						}

					}


					db.setAutoCommit(false);

					prep = db.prepareStatement("insert into Wreck (Person, Lat, Lon, Time, LargestAccel) values(?, ?, ?, ?,?);");
					prep.setInt(1, id);
					prep.setDouble(2, report.getLatitude());
					prep.setDouble(3, report.getLongitude());
					prep.setDate(4, new Date(report.getTime()));
					prep.setDouble(5, report.getAcceleration());
					prep.addBatch();
					prep.executeBatch();

					db.commit();

					prep = db.prepareStatement("select ContactId from EmergencyContacts where PersonId = ?");
					prep.setInt(1, id);
					ArrayList<String> nums = new ArrayList<String>();
					rs = prep.executeQuery();
					while(rs.next()){
						nums.add(rs.getString("ContactId"));
					}
					rs.close();
					
					db.close();

					for(String s:nums){
						SMSSender.sendText(s);
						AsteriskConnector.playRecordingToPSTNNumber(s);
					}
					response = new AccidentHandledNotification();

				} catch (SQLException e) {
					logger_.log(Level.SEVERE,
							"SQLException: ", e);
					if (db != null){
						try {
							db.close();
						} catch (SQLException e1) {
							
							e1.printStackTrace();
						}
					}
				}

			}




			//do something with the report
		} catch (AccidentFormatException e) {
			logger_.log(Level.SEVERE,
					"Error extracting accident report from notification", e);
		}
		return response;
	}

	public AccidentParser getParser() {
		return parser_;
	}

	public void setParser(AccidentParser parser) {
		parser_ = parser;
	}


}
