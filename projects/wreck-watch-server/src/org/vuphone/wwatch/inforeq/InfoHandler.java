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
package org.vuphone.wwatch.inforeq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;
import org.vuphone.wwatch.routing.Waypoint;

public class InfoHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(InfoHandler.class.getName());
	
	private DataSource ds_;

	public Notification handle(Notification n) {
		InfoNotification info = (InfoNotification)n;

		
		Connection db = null;

		try {
			db = ds_.getConnection();
			db.setAutoCommit(true);
		} catch (SQLException e) {

			logger_.log(Level.SEVERE,
					"SQLException: ", e);
		}


		String sql = "select * from Wreck where lat between ? and ? and lon between ? and ?;"; 
		InfoHandledNotification note;
		try{
			PreparedStatement prep = db.prepareStatement(sql);
			prep.setDouble(1, info.getTopLeftCorner().getLatitude());
			prep.setDouble(2, info.getBottomLeftCorner().getLatitude());
			prep.setDouble(3, info.getTopLeftCorner().getLongitude());
			prep.setDouble(4, info.getTopRightCorner().getLongitude()); 
			

			note = new InfoHandledNotification();
			note.newRoute();

			//Get the wreck id
			ArrayList<Integer> ids = new ArrayList<Integer>();
			ResultSet rs = prep.executeQuery();
			while (rs.next()){
				ids.add(rs.getInt("WreckID"));
			}
			rs.close();

			sql = "select * from Route where WreckID = ?";
			for (Integer i:ids){
				prep = db.prepareStatement(sql);
				prep.setInt(1, i);
				
				rs = prep.executeQuery();
				
				while(rs.next()){
					note.addWaypoint(new Waypoint(rs.getDouble("Lat"), rs.getDouble("Lon"), rs.getLong("Time")));
				}
				rs.close();
				note.newRoute();
				
			}
			
			db.close();
			return note;
		}catch (SQLException e) {
			logger_.log(Level.SEVERE,
					"SQLException: ", e);
		}

		return null;
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	public DataSource getDataConnection(){
		return ds_;
	}


	public static void main(String[] args){
		InfoNotification n = new InfoNotification();
		n.setBottomLeftCorner(34.00, -90);
		n.setBottomRightCorner(34.00, -70);
		n.setTopLeftCorner(37.00, -90);
		n.setTopRightCorner(37.00, -70);

		InfoHandler h = new InfoHandler();
		Notification ihn = h.handle(n);
		ihn = (InfoHandledNotification)ihn;

	}

}
