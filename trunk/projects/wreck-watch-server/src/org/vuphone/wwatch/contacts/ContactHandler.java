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
package org.vuphone.wwatch.contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.inforeq.InfoParser;
import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ContactHandler implements NotificationHandler {
	private static final Logger logger_ = Logger.getLogger(ContactHandler.class
			.getName());

	// XML will instantiate these
	private ContactParser parser_;
	private DataSource ds_;

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	public Notification handle(Notification n) {

		Connection db = null;

		try {
			db = ds_.getConnection();
			db.setAutoCommit(true);
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "SQLException connecting to database: "
					+ e.getMessage());
			return n;
		}

		ContactNotification cn = null;
		try {
			cn = parser_.getContact(n.getRequest());
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to parse the notification, stopping");
			closeDatabase(db);
			return n;
		}

		PreparedStatement prep;
		int userId = 0;
		try {
			prep = db
					.prepareStatement("select id from People where AndroidId like ?");
			prep.setString(1, cn.getAndroidID());
			ResultSet rs = prep.executeQuery();
			rs.next();
			userId = rs.getInt("id");
			rs.close();
		} catch (SQLException e) {
			try {
				prep = db
						.prepareStatement("insert into People (androidid) values (?)");
				prep.setString(1, cn.getAndroidID());
				prep.execute();
				prep = db
						.prepareStatement("select id from People where AndroidId like ?");
				prep.setString(1, cn.getAndroidID());
				ResultSet rs = prep.executeQuery();
				rs.next();
				userId = rs.getInt("id");
				rs.close();
			} catch (SQLException ex) {
				logger_.log(Level.SEVERE, "SQLException retrieving userid: "
						+ ex.getMessage());
				closeDatabase(db);
				return n;
			}
		}

		String[] contacts = cn.getNumbers();

		try {
			db.setAutoCommit(false);
			prep = db
					.prepareStatement("delete from EmergencyContacts where PersonId = ?");
			prep.setInt(1, userId);
			prep.executeUpdate();

			prep = db
					.prepareStatement("insert into EmergencyContacts (PersonId, ContactId) values(?,?)");
			for (String s : contacts) {
				prep.setInt(1, userId);
				prep.setString(2, s);
				prep.addBatch();
			}

			prep.executeBatch();

			db.commit();
			closeDatabase(db);

		} catch (SQLException e) {
			logger_.log(Level.SEVERE,
					"SQLException creating emergency contacts: "
							+ e.getMessage());
			closeDatabase(db);
		}

		return cn;
	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

}
