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
package org.vuphone.vandyupon.notification.eventrequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.vuphone.vandyupon.datastructs.Event;
import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventRequestHandler implements NotificationHandler {

	private DataSource ds_;

	/*
	 * Radius of the earth to use in calculations.  This must be
	 * in the same units as the distance you wish to calculate 
	 */
	private static final double RADIUS_EARTH = 20925646.3; //feet
	//private static final double RADIUS_EARTH = 3963.191; //miles


	@Override
	public ResponseNotification handle(Notification n) throws HandlerFailedException {

		if (!(n instanceof EventRequest)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}

		EventRequest req = (EventRequest)n;


		try {
			EventRequestResponse err = new EventRequestResponse(req.getResponseType(), req.getCallback());
			HashMap<Integer, Event> events = new HashMap<Integer, Event>(); 
			Connection db = ds_.getConnection();
			
			/*
			 * The arguments for this statement are as follows:
			 * 1: Current time in milliseconds
			 * 2: Radius of the earth in the appropriate unites
			 * 3: Latitude of the anchor point
			 * 4: Latitude of the anchor point
			 * 5: Longitude of the anchor point
			 * 6: Radius requested
			 */
			String sql = "create temporary table evtstmp select eventid as id, events.name as name, starttime, endtime, events.userid as user, lat, lon " +
			"from events inner join locations on events.locationid = locations.locationid " +
			"where endtime > ? and ? * ACOS( (SIN( PI() * ? / 180) * SIN( PI() * lat/180) ) + (COS ( PI() * ? /180) * " +
			"COS (PI() * lat /180) * COS ( PI() * lon/180 - PI() * ?/180))) < ?";
			

			PreparedStatement prep = db.prepareStatement(sql);

			prep.setLong(1, System.currentTimeMillis());
			prep.setDouble(2, RADIUS_EARTH);
			prep.setDouble(3,req.getAnchor().getLat());
			prep.setDouble(4,req.getAnchor().getLat());
			prep.setDouble(5,req.getAnchor().getLon());
			prep.setDouble(6, req.getDistance());

			prep.executeUpdate();
			
			sql = "select id, name, starttime, endtime, deviceid, lat, lon from evtstmp inner join people on user = userid";
			
			prep = db.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			
			while (rs.next()){
				Event e = new Event();
				e.setName(rs.getString("name"));
				e.setID(rs.getInt("id"));
				e.setStartTime(rs.getLong("starttime"));
				e.setEndTime(rs.getLong("endtime"));
				e.setLocation(new Location(rs.getDouble("lat"), rs.getDouble("lon")));
				if (rs.getString("deviceid").equalsIgnoreCase(req.getUserId())){
					//this user owns this event
					e.setIsOwner(true);
				}else {
					e.setIsOwner(false);
				}
				err.addEvent(e);
				events.put(e.getID(), e);
			}

			rs.close();
			return err;
			
		} catch (SQLException e) {
			e.printStackTrace();
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}		

	}

	public void setDataConnection(DataSource ds){
		ds_ =  ds;
	}

	public DataSource getDataConnection(){
		return ds_;
	}

}
