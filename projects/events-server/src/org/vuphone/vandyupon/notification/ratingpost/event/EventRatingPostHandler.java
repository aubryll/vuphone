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
package org.vuphone.vandyupon.notification.ratingpost.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventRatingPostHandler implements NotificationHandler{
	
	private DataSource ds_;

	
	public ResponseNotification handle(Notification n)
			throws HandlerFailedException {
		
		if (!(n instanceof EventRatingPost)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		EventRatingPost erp = (EventRatingPost) n;
		String message;
		
		try{
			Connection db = ds_.getConnection();
			String sql = "select userid from people where deviceid like ?";
			PreparedStatement prep = db.prepareStatement(sql);
			prep.setString(1, erp.getUser());
			
			ResultSet rs = prep.executeQuery();
			long id;
			rs.next();
			try{
				id = rs.getLong(1);
			}catch (SQLException e){
				sql = "insert into people (deviceid) values (?)";
				prep = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				prep.setString(1, erp.getUser());
				
				prep.executeUpdate();
				rs = prep.getGeneratedKeys();
				rs.next();
				id = rs.getLong(1);
			}
			
			sql = "select * from eventrating where userid = ? and eventid = ?";
			prep = db.prepareStatement(sql);
			prep.setLong(1, id);
			prep.setLong(2, erp.getEvent());
			
		    rs = prep.executeQuery();
			if (rs.next()){
				message = "You have already submitted a rating for this event.";
			}else {
				sql = "insert into eventrating(userid, eventid, value, comment, " +
						"submissiondate) values(?,?,?,?,?)";
				prep = db.prepareStatement(sql);
				prep.setLong(1, id);
				prep.setLong(2, erp.getEvent());
				prep.setString(3, erp.getValue());
				prep.setString(4, erp.getComment());
				prep.setLong(5, System.currentTimeMillis());
				
				if (prep.executeUpdate() != 0){
					message = "Your rating has been saved";
				}else{
					message = "A database error has occurred";
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			message = "A database error has occurred";
		}
		
		return new EventRatingPostResponse(message, erp.getResponseType(), erp.getCallback());
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}

}
