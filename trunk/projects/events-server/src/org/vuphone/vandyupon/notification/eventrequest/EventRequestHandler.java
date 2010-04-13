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
import java.util.List;

import javax.sql.DataSource;

import org.vuphone.vandyupon.datastructs.Event;
import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

// TODO - for now any tags are applied in an "OR" manner - aka if any of the tags are present the event is returned. Perhaps later the request could include other orderings as well
public class EventRequestHandler implements NotificationHandler {

	private DataSource ds_;

	/*
	 * Radius of the earth to use in calculations. This must be in the same
	 * units as the distance you wish to calculate
	 */
	private static final double RADIUS_EARTH = 20925646.3; // feet

	// private static final double RADIUS_EARTH = 3963.191; //miles

	public ResponseNotification handle(Notification n)
			throws HandlerFailedException {

		if (!(n instanceof EventRequest)) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}

		EventRequest req = (EventRequest) n;

		try {
			EventRequestResponse response = new EventRequestResponse(req
					.getResponseType(), req.getCallback());
			HashMap<Integer, Event> events = new HashMap<Integer, Event>();
			Connection db = ds_.getConnection();

			/*
			 * The arguments for this statement are as follows: 1: Current time
			 * in milliseconds 2: Radius of the earth in the appropriate unites
			 * 3: Latitude of the anchor point 4: Latitude of the anchor point
			 * 5: Longitude of the anchor point 6: Radius requested 7: UID used
			 * by the caller to identify the event
			 */
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("CREATE TEMPORARY TABLE evtstmp ");
			sqlBuffer.append("SELECT ");

			sqlBuffer.append("eventid AS id, ");
			sqlBuffer.append("events.name AS name, ");
			sqlBuffer.append("starttime, endtime, ");
			sqlBuffer.append("events.userid AS user, ");
			sqlBuffer.append("locations.locationid AS locid, ");
			sqlBuffer.append("locations.name AS locname, ");
			sqlBuffer.append("lat, lon, ");
			sqlBuffer.append("events.lastupdate AS lastupdate, ");
			sqlBuffer.append("events.sourceuid AS sourceuid ");

			sqlBuffer.append("FROM events INNER JOIN locations ");
			sqlBuffer.append("ON events.locationid = locations.locationid ");

			sqlBuffer.append("WHERE ");
			sqlBuffer.append("endtime > ? "); // current time in ms
			sqlBuffer.append("AND events.lastupdate > ? "); // lastupdate in ms
			// endtime>query.starttime & starttime < q.endtime
			sqlBuffer.append("AND (endtime > ? AND starttime < ?) ");
			// Radius of the earth in the appropriate unites
			// Latitude of the anchor point
			// Latitude of the anchor point
			// Longitude of the anchor point
			// Radius requested
			sqlBuffer
					.append("AND (? * ACOS( (SIN( PI() * ? / 180) * SIN( PI() * lat/180) ) + (COS( PI() * ? /180) * COS(PI() * lat /180) * COS( PI() * lon/180 - PI() * ?/180))) < ? ");
			sqlBuffer.append("OR lat IS NULL)");

			PreparedStatement prep = db.prepareStatement(sqlBuffer.toString());

			prep.setLong(1, System.currentTimeMillis() / 1000);
			prep.setLong(2, req.getUpdateTime());
			prep.setLong(3, req.getStartTime());
			prep.setLong(4, req.getEndTime());

			prep.setDouble(5, RADIUS_EARTH);
			prep.setDouble(6, req.getAnchor().getLat());
			prep.setDouble(7, req.getAnchor().getLat());
			prep.setDouble(8, req.getAnchor().getLon());
			prep.setDouble(9, req.getDistance());

			prep.executeUpdate();

			// =========================================================================
			// Now that the events have been mainly filtered, we would like to
			// filter
			// them further by metatags, and add some of the meta-info back into
			// the
			// results
			// TODO - Make this less atrocious by separating up the methods some
			// =========================================================================

			// Given the filtered list of events, further filter by adding
			// metatags
			sqlBuffer = new StringBuffer();
			sqlBuffer.append("SELECT ");
			sqlBuffer.append("id, name, starttime, endtime, deviceid, ");
			sqlBuffer.append("locid, locname, lat, lon, lastupdate, ");
			sqlBuffer.append("sourceuid, description ");

			sqlBuffer.append("FROM evtstmp ");

			// Always add the source user ID to the return
			sqlBuffer.append("INNER JOIN people ");
			sqlBuffer.append("ON user = userid ");

			// Add any requested tags to the filter
			List<String> tags = req.getTags();
			if (tags.size() > 0) {
				sqlBuffer.append("INNER JOIN ");
				sqlBuffer.append("(SELECT DISTINCT eventid ");
				sqlBuffer.append("	FROM eventmeta ");
				sqlBuffer.append("	WHERE metatype=5 AND (");

				String tag;
				for (int i = 0; i < tags.size(); i++) {
					tag = tags.get(i);
					sqlBuffer.append("value LIKE '");
					sqlBuffer.append(tag);
					sqlBuffer.append("' ");

					// TODO - ensure there is not an off by one!
					if (i != tags.size() - 1)
						sqlBuffer.append("OR ");
				}
				sqlBuffer.append("	)");
				sqlBuffer.append(") AS foo ");
				sqlBuffer.append("ON id = foo.eventid ");
			}

			// Always a description, if there is one
			sqlBuffer.append("LEFT JOIN ");
			sqlBuffer.append("	(SELECT ");
			sqlBuffer.append("	eventid, `value` as description ");
			sqlBuffer.append("	FROM eventmeta ");
			sqlBuffer.append("	WHERE metatype=");
			sqlBuffer
					.append("		(SELECT typeid FROM metatypes WHERE typename LIKE 'description')");
			sqlBuffer.append("	) AS bar ");
			sqlBuffer.append("ON id = bar.eventid;");

			prep = db.prepareStatement(sqlBuffer.toString());
			
			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				Event e = new Event();
				e.setName(rs.getString("name"));
				e.setID(rs.getInt("id"));
				e.setStartTime(rs.getLong("starttime"));
				e.setEndTime(rs.getLong("endtime"));
				e.setLocation(new Location(rs.getInt("locid"), rs
						.getString("locname"), rs.getDouble("lat"), rs
						.getDouble("lon")));
				if (rs.getString("deviceid").equalsIgnoreCase(req.getUserId())) {
					// this user owns this event
					e.setIsOwner(true);
				} else {
					e.setIsOwner(false);
				}
				e.setLastUpdate(rs.getLong("lastupdate"));
				e.setSourceUid(rs.getString("sourceuid"));
				e.setDescription(rs.getString("description"));
				response.addEvent(e);
				events.put(e.getID(), e);
			}

			rs.close();
			return response;

		} catch (SQLException e) {
			e.printStackTrace();
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}

	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

}
