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
package org.vuphone.wwatch.mapping.wrecklocationrequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.inforeq.InfoHandler;
import org.vuphone.wwatch.mapping.MapEvent;
import org.vuphone.wwatch.mapping.MapEventHandler;
import org.vuphone.wwatch.mapping.MapResponse;
import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.routing.Waypoint;

public class WreckLocationRequestHandler implements MapEventHandler {

	private static final Logger logger_ = Logger.getLogger(InfoHandler.class
			.getName());

	private DataSource ds_;

	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	public MapResponse handle(MapEvent e) throws HandlerFailedException {	

		WreckLocationRequestEvent wlre = null;
		if (e instanceof WreckLocationRequestEvent){
			wlre = (WreckLocationRequestEvent)e;
		}else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new Exception("MapEvent/Hadler Mismatch:  WreckLocationRequestHandler cannot handle: " + e.getClass()));
			throw hfe;
		}
		Connection db = null;

		try {
			db = ds_.getConnection();
			db.setAutoCommit(true);
		} catch (SQLException ex) {
			logger_.log(Level.SEVERE, "SQLException: ", e);
			closeDatabase(db);
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(ex);
			throw hfe;
		}

		// Prepare the SQL select
		// Execute the select
		// Add the results to the InfoHandledNotification
		String sql = "select * from Wreck where lat between ? and ? and lon between ? and ?;";
		WreckLocationResponse wlr;

		try {
			PreparedStatement prep = db.prepareStatement(sql);
			prep.setDouble(1, wlre.getSouthWestLat());
			prep.setDouble(2, wlre.getNorthEastLat());
			prep.setDouble(3, wlre.getSouthWestLon());
			prep.setDouble(4, wlre.getNorthEastLon());

			wlr = new WreckLocationResponse();
			wlr.newRoute();

			// Get the wreck id
			ArrayList<Integer> ids = new ArrayList<Integer>();
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("WreckID"));
			}
			rs.close();

			sql = "select * from Route where WreckID = ?";
			for (Integer i : ids) {
				prep = db.prepareStatement(sql);
				prep.setInt(1, i);

				rs = prep.executeQuery();

				while (rs.next()) {
					wlr.addWaypoint(new Waypoint(rs.getDouble("Lat"), rs
							.getDouble("Lon"), rs.getLong("Time")));
				}
				rs.close();
				wlr.newRoute();

			}
			closeDatabase(db);
			return wlr;
		}catch (SQLException e2) {
			logger_.log(Level.SEVERE, "SQLException: ", e);
			closeDatabase(db);
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e2);
			throw hfe;
		}
	}

	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}

	public DataSource getDataConnection(){
		return ds_;
	}

}
