package org.vuphone.vandyupon.notification.diningpost;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.vuphone.vandyupon.notification.diningrating.DiningRating;
import org.vuphone.vandyupon.notification.diningrating.DiningRatingRequest;
import org.vuphone.vandyupon.notification.eventpost.EventPost;

public class DiningPostHandler implements NotificationHandler {
	
	private DataSource ds_;
	
	public DataSource getDataConnection() {
		return ds_;
	}
	
	private int addRating(DiningRating dp) throws SQLException {
		String sql;
		
		Connection conn = ds_.getConnection();
		
		sql = "INSERT INTO ratings (deviceid, loc, rating) VALUES (?, ?, ?)";
		
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, dp.getDeviceID());
		prep.setInt(2, dp.getLocation());
		prep.setLong(3, dp.getRating());
		
		ResultSet rs = prep.executeQuery();
		rs.next();
		
		if (prep.executeUpdate() == 0) {
			throw new SQLException(
					"Insertion into vandyupon.dining failed for an unknown reason");
		} else/*Everything Worked*/ {
			rs = prep.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			prep.execute();
			return id;
		}
		
	}
	
	// This method is used to see if the request is already in the Database
	private boolean checkForEntry(DiningRating dp) throws SQLException {
		
		String sql;
		
		Connection conn = ds_.getConnection();
		
		sql = "SELECT * FROM people WHERE deviceid LIKE ? AND loc LIKE ?";
		
		PreparedStatement prep = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		
		prep.setString(1, dp.getDeviceID());
		
		Integer location = new Integer(dp.getLocation());
		String loc = location.toString();
		prep.setString(2, loc);
		
		ResultSet rs = prep.executeQuery();
		
		try{
			rs.next();
			int exists = rs.getInt(1);
			if (exists != 0) {
				rs.close();
				return true;
			} else/* The ID is not in the database with the current location*/ {
				rs.close();
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	
	public ResponseNotification handle(Notification n) throws HandlerFailedException {
		if (!(n instanceof DiningPost)) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		DiningPost dp = (DiningPost) n;
		
	}

}
