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
package org.vuphone.wwatch.routing.handsetrouterequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.inforeq.InfoHandler;
import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;

import com.thoughtworks.xstream.XStream;

public class RouteRequestHandler implements NotificationHandler {
	
	private static final Logger logger_ = Logger.getLogger(InfoHandler.class
			.getName());
	
	private DataSource ds_;
	private RouteRequestParser parser_ = new RouteRequestParser();
	
	private RouteRequestHandledNotification fillResponse(RouteRequestHandledNotification rrn){
		
		XStream xs  = new XStream();
		xs.alias("Route", Route.class);
		xs.addImplicitCollection(Route.class, "route_", "Point", Waypoint.class);
		xs.aliasField("Latitude", Waypoint.class, "latitude_");
		xs.aliasField("Longitude", Waypoint.class, "longitude_");
		xs.aliasField("Time", Waypoint.class, "timeStamp_");
		xs.omitField(Route.class, "curIndex_");
		xs.omitField(Route.class, "accidentID_");
		rrn.setResponse(xs.toXML(rrn.getRoute()));
		return rrn;		
		
	}

	public Notification handle(Notification n) throws HandlerFailedException {
		RouteRequestNotification rn;
		if (n.getType().equalsIgnoreCase("routeGet")){
			rn = parser_.parse(n.getRequest());
		}else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new Exception("Incompatible argument"));
			throw hfe;
		}

		RouteRequestHandledNotification note = new RouteRequestHandledNotification();
		Connection db;
		
		try {
			db = ds_.getConnection();
		} catch (SQLException e1) {
			logger_.log(Level.SEVERE, "Could not establish database connection.");
			e1.printStackTrace();
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e1);
			throw hfe;

		}
		PreparedStatement prep;
		ResultSet rs;
		String sql = "select * from route where WreckID = ?";
		
		
		try {
			
			prep = db.prepareStatement(sql);
			prep.setLong(1, rn.getId());

			rs = prep.executeQuery();

			while (rs.next()) {
				note.addWaypoint(new Waypoint(rs.getDouble("Lat"), rs
						.getDouble("Lon"), rs.getLong("Date")));
			}
			rs.close();


			db.close();
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "Error accessing database.");
			e.printStackTrace();
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
		return fillResponse(note);
	}

	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}

	public DataSource getDataConnection(){
		return ds_;
	}

}
