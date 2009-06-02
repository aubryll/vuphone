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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;
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

	private AccidentParser parser_;

	/**
	 * This method receives a notification of an accident,
	 * converts the notification into an AccidentReport,
	 * and then determines the proper way to respond to the
	 * accident.
	 */
	public Notification handle(Notification n) {
		Notification response = null;
		try {
			AccidentReport report = parser_.getAccident(n);
			Connection db = null;
			
			try {
				db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");
				db.setAutoCommit(false);
			} catch (SQLException e) {
				
				logger_.log(Level.SEVERE,
						"SQLException: ", e);
			}
			
			if (db != null){
				String sql="define @user integer;" +
						"select @user = id from People where Email like (?);" +
						"insert into Wreck (Person, Lat, Lon, Time, LargestAccel) values(@user, ?, ?, ?,?);" +
						"define @wid integer;";
						
				try {
					PreparedStatement prep = db.prepareStatement(sql);
					prep.setString(1, report.getParty());
					
					prep.setDouble(2, report.getLatitude());
					prep.setDouble(3, report.getLongitude());
					prep.setDate(4, new Date(report.getTime()));
					prep.setDouble(5, report.getAcceleration());
					
					prep.addBatch();
					
					prep.execute();
					db.commit();
					
					sql = "define @wid integer;" +
							"select @wid = max(wreckid) from Wreck;" +
							"insert into Route(wreckid, lat, lon, time) values(" +
							"@wid, ?, ?, ?);";
					prep = db.prepareStatement(sql);
					Route route = report.getRoute();
					while (route.peek() != null){
						Waypoint temp = route.getNextPoint();
						prep.setDouble(1, temp.getLatitude());
						prep.setDouble(2, temp.getLongitude());
						prep.setLong(3, temp.getTime());
						prep.addBatch();
					}
					
					prep.execute();
					db.commit();
					db.close();
					
				} catch (SQLException e) {
					
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
