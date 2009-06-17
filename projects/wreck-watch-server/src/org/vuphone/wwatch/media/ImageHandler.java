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
	private ImageParser parser_;
	
	public Notification handle(Notification n) {



		Connection db = null;

		try {
			db = ds_.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to initialize org.sqlite.JDBC");
			logger_.log(Level.SEVERE,
					" Unable to continue without a database, stopping");
			return n;
		}
		
		
		// Parse the Notification and extract the AccidentReport
//		AccidentNotification report;
//		try {
//			report = parser_.getAccident(n);
//		} catch (AccidentFormatException e1) {
//			e1.printStackTrace();
//			logger_.log(Level.SEVERE,
//					"Unable to parse the Notification:");
//			logger_.log(Level.SEVERE, " Notification was: ");
//			logger_.log(Level.SEVERE, " " + n.toString());
//			logger_.log(Level.SEVERE,
//					" Unable to continue without AccidentReport, stopping");
//			return n;
//		}
//		
		
		return null;
	}
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}
	
	public ImageParser getParser() {
		return parser_;
	}
	
	public void setParser(ImageParser p) {
		parser_ = p;
	}


}
