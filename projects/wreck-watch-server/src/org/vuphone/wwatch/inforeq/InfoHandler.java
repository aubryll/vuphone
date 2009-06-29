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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serializer.dom3.LSSerializerImpl;
import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSSerializer;

public class InfoHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(InfoHandler.class
			.getName());

	// XML will instantiate these
	private InfoParser parser_;
	private DataSource ds_;

	private InfoHandledNotification buildResponse(InfoHandledNotification info) {

		// Build the xml response
		Document d = null;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
		} catch (ParserConfigurationException e) {

			logger_
					.log(
							Level.SEVERE,
							"Parser configuration exception creating document for xml response",
							e);
		}
		Node rootRt = d.createElement("Routes");

		for (Route r : info.getAccidents()) {
			Node route = d.createElement("Route");
			Node rootPt = d.createElement("Points");

			for (Waypoint w : r.getRoute()) {
				Node pointR = d.createElement("Point");
				Node lat = d.createElement("Latitude");

				lat.appendChild(d.createTextNode(Double.toString(w
						.getLatitude())));

				pointR.appendChild(lat);

				Node lon = d.createElement("Longitude");
				lon.appendChild(d.createTextNode(Double.toString(w
						.getLongitude())));

				pointR.appendChild(lon);

				Node time = d.createElement("Time");
				time.appendChild(d.createTextNode(Long.toString(w.getTime())));

				pointR.appendChild(time);

				rootPt.appendChild(pointR);
			}
			route.appendChild(rootPt);
			
			Node id = d.createElement("id");
			id.appendChild(d.createTextNode(Integer.toString(r.getAccidentId())));
			route.appendChild(id);
			
			rootRt.appendChild(route);

		}
		d.appendChild(rootRt);
		LSSerializer ls = new LSSerializerImpl();
		String xml = ls.writeToString(d);

		info.setResponse(xml);
		return info;
	}

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	private InfoHandledNotification getInfo(final InfoNotification info)
			throws HandlerFailedException {

		Connection db = null;

		try {
			db = ds_.getConnection();
			db.setAutoCommit(true);
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "SQLException: ", e);
			closeDatabase(db);
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}

		// Prepare the SQL select
		// Execute the select
		// Add the results to the InfoHandledNotification
		String sql = "select * from Wreck where lat between ? and ? and lon between ? and ?;";
		InfoHandledNotification note;

		try {
			PreparedStatement prep = db.prepareStatement(sql);
			
			prep.setDouble(1, info.getMinLatitude());
			prep.setDouble(2, info.getMaxLatitude());
			
			prep.setDouble(3, info.getMinLongitude());
			prep.setDouble(4, info.getMaxLongitude());
			
			note = new InfoHandledNotification();

			// Get the wreck id
			ArrayList<Integer> ids = new ArrayList<Integer>();
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("WreckID"));
			}
			rs.close();

			sql = "select * from Route where WreckID = ?";
			for (Integer i : ids) {
				note.newRoute();
				note.setCurrentAccidentId(i);
				prep = db.prepareStatement(sql);
				prep.setInt(1, i);

				rs = prep.executeQuery();

				while (rs.next()) {
					note.addWaypoint(new Waypoint(rs.getDouble("Lat"), rs
							.getDouble("Lon"), rs.getLong("Date")));
				}
				rs.close();
				

			}

			db.close();
			return note;

		} catch (SQLException e) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
	}

	/**
	 * This method uses a parser to attempt to convert a generic Notification
	 * into an InfoNotification, and then builds the correct reply inside of an
	 * InfoHandledNotificaiton
	 */
	public Notification handle(Notification n) throws HandlerFailedException {

		InfoNotification info = null;
		try {
			info = parser_.getInfo(n.getRequest());
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to parse the notification, stopping");
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(ife);
			throw hfe;
		}

		InfoHandledNotification note = getInfo(info);

		note = buildResponse(note);
		return note;

	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

	public void setParser(InfoParser p) {
		parser_ = p;
	}

	public InfoParser getParser() {
		return parser_;
	}
}
