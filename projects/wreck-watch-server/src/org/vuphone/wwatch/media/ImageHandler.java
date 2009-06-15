package org.vuphone.wwatch.media;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vuphone.wwatch.accident.AccidentReport;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageHandler implements NotificationHandler{

	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	
	public Notification handle(Notification n) {
		Notification response = null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}


		Connection db = null;

		try {
			db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");
			db.setAutoCommit(true);
		} catch (SQLException e) {

			logger_.log(Level.SEVERE,
					"SQLException: ", e);
		}
		
		return response;
	}

}
