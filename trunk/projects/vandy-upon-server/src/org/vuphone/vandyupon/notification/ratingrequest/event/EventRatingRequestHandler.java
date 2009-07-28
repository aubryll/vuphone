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
package org.vuphone.vandyupon.notification.ratingrequest.event;

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

public class EventRatingRequestHandler implements NotificationHandler{
	
	private DataSource ds_;
	
	@Override
	public ResponseNotification handle(Notification n)
			throws HandlerFailedException {
		
		if (!(n instanceof EventRatingRequest)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		EventRatingRequest err = (EventRatingRequest) n;
		try{
			Connection c = ds_.getConnection();
			EventRating er = new EventRating();
			String sql = "select *  from eventrating where eventid = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setLong(1, err.getId());
			
			ResultSet rs = prep.executeQuery();
			
			while (rs.next()){
				er.addRating(rs.getLong("userid"), rs.getInt("value"), 
						rs.getString("comment"), rs.getLong("submissiondate"));
			}
			
			rs.close();
			
			EventRatingResponse resp = new EventRatingResponse(err.getId(), 
					err.getResponseType(), err.getCallback());
			resp.setRating(er);
			return resp;
		}catch (SQLException e){
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
