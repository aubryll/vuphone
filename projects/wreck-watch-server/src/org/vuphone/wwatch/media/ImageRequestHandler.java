package org.vuphone.wwatch.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageRequestHandler implements NotificationHandler{
	
	private static final Logger logger_ = Logger
	.getLogger(ImageRequestHandler.class.getName());
	private DataSource ds_;
	
	public Notification handle(Notification n) {
		Notification response = null;
		
		try {
			ImageRequestNotification irn = (ImageRequestNotification)n;
			Connection db = null;

			try {
				db = ds_.getConnection();
				db.setAutoCommit(true);
			} catch (SQLException e) {
				db.close();
				logger_.log(Level.SEVERE,
						"SQLException: ", e);
			}
			if (db != null){

				int id = 0;
				try {
					PreparedStatement prep = db.prepareStatement("select WreckID from Wreck where Lat like ? and Lon like ?;");
					prep.setDouble(1, irn.getLat());
					prep.setDouble(2, irn.getLon());

					ResultSet rs  = prep.executeQuery();

					rs.next();
					id = rs.getInt("WreckID");
					rs.close();

					prep = db.prepareStatement("select * from Images where WreckID like ?;");
					prep.setInt(1, id);
					
					rs = prep.executeQuery();
					
					while (rs.next()) {
						//TODO - extract either the images themselves or their
						//locations in the file system, depending on how they are 
						//stored in the database.
					}
					
					//TODO - might have to add an extra step here to get the images
					//out of the file system.
				
				}catch (SQLException e) {
					logger_.log(Level.SEVERE,
							"SQLException: ", e);
					db.close();
					return null;
				}	
			}
		}catch (Exception sqle) {
			return null;
		}
		
		//TODO - find some way to format the returned images as a notification
		//object.  Will probably have to create some type of
		//ImageRequestHandledNotification class to do this.
		
		return response;
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}

}
