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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

import com.thoughtworks.xstream.XStream;

public class InfoHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(InfoHandler.class
			.getName());

	// XML will instantiate these
	private InfoParser parser_;
	private DataSource ds_;

	private InfoHandledNotification buildResponse(InfoHandledNotification info) {
		XStream xs = new XStream();
		xs.alias("Wrecks", InfoHandledNotification.class);
		xs.aliasField("Latitude", Wreck.class, "lat_");
		xs.aliasField("Longitude", Wreck.class, "lon_");
		xs.aliasField("id", Wreck.class, "id_");
		xs.aliasField("Time", Wreck.class, "time_");
		xs.omitField(InfoHandledNotification.class, "response_");
		xs.omitField(Notification.class, "type_");
		xs.addImplicitCollection(InfoHandledNotification.class, "accidents_", "Wreck", Wreck.class);

		info.setResponse(xs.toXML(info));
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
		String sql = "select * from wreck where lat between ? and ? and lon between ? and ? and Date>?";
		InfoHandledNotification note;

		try {
			PreparedStatement prep = db.prepareStatement(sql);
			
			prep.setDouble(1, info.getMinLatitude());
			prep.setDouble(2, info.getMaxLatitude());
			
			prep.setDouble(3, info.getMinLongitude());
			prep.setDouble(4, info.getMaxLongitude());
			
			prep.setLong(5, info.getTime());
			
			note = new InfoHandledNotification();

			
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				note.addWreck(rs.getDouble("Lat"), rs.getDouble("Lon"), rs.getInt("WreckID"),rs.getLong("Date"));
			}
			rs.close();

			
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
