package org.vuphone.wwatch.media;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageHandler implements NotificationHandler{

	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private DataSource ds_;
	
	public Notification handle(Notification n) {
		Notification response = null;



		Connection db = null;

		try {
			db = ds_.getConnection();
		} catch (SQLException e) {

			logger_.log(Level.SEVERE,
					"SQLException: ", e);
		}
		
		return response;
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}


}
