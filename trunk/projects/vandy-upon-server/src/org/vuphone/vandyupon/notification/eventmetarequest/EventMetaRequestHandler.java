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
package org.vuphone.vandyupon.notification.eventmetarequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventMetaRequestHandler implements NotificationHandler {

	private DataSource ds_;

	@Override
	public ResponseNotification handle(Notification n) throws HandlerFailedException {
		
		if (!(n instanceof EventMetaRequest)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		EventMetaRequest emr = (EventMetaRequest)n;
		
		try{
			EventMetaRequestResponse emresp = new EventMetaRequestResponse(
					emr.getEvent(), emr.getResponseType(), emr.getCallback());

			Connection db = ds_.getConnection();

			String sql = "select value, typename from eventmeta inner join metatypes on " +
					"metatype = typeid where eventid =?";

			PreparedStatement prep = db.prepareStatement(sql);
			prep.setInt(1, emr.getEvent());

			ResultSet rs = prep.executeQuery();
			
			while (rs.next()){
				emresp.addMeta(rs.getString("typename"), rs.getString("value"));
			}
			rs.close();
			
			return emresp;
		}catch(SQLException e){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
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
