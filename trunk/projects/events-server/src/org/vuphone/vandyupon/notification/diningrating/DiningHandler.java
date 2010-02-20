// Copyright 2010 Michael Fagan

package org.vuphone.vandyupon.notification.diningrating;

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

// This class handles the two types of DiningRatings, either DiningRating or DiningRatingRequest.
// It performs the necessary operations on the Database and returns the appropriate Response type
// for each call.
public class DiningHandler implements NotificationHandler {
	
	// The datasource, whatever that is!
	private DataSource ds_;
	
	// This method gets the SQL connection.
	public DataSource getDataConnection() {
		return ds_;
	}
	
	// This method adds a rating to the database.
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
		} else/* Everything Worked! */ {
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
			} else/* The ID is not in the database with the current location */ {
				rs.close();
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	// This method performs the SQL to get the rating for a particular location.
	private int getRating(DiningRatingRequest drr) throws SQLException {
		String sql;
		
		Connection conn = ds_.getConnection();
		
		sql = "SELECT AVG(rating) FROM DiningRatings WHERE loc=?";
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, drr.getLocation());
		
		ResultSet rs = prep.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
	
	// This method is used to check what type of DiningRating we have.
	private int checkForType(Notification n) {
		if(n instanceof DiningRating)
			return 1;
		if(n instanceof DiningRatingRequest)
			return 2;
		else // The type is neither, error
			return 0;
	}
	
	// This method actually handles the DiningRatings and DiningRatingRequests.
	// We perform the proper functions based on the type we receive.  After that,
	// we return the proper response type.  If we do not receive a type we know 
	// how to deal with, then we return null.
	public ResponseNotification handle(Notification n) {
		
		if(checkForType(n) == 1)/* The type is DiningRating */{
			DiningRating dr = (DiningRating) n;
			
			try{
				if(!(checkForEntry(dr))){
					addRating(dr);
					return new DiningRatingResponse(null, null, true);
				}
			}
			catch(SQLException e){
				return new DiningRatingResponse(null, null, false);
			}
		}
		else if(checkForType(n) == 2)/* The type is DiningRatingRequest */{
			DiningRatingRequest drr = (DiningRatingRequest) n;
			
			try{
				int avgRating = getRating(drr);
				return new DiningRatingRequestResponse(null, null, avgRating, true);
			}
			catch(SQLException e){
				return new DiningRatingRequestResponse(null, null, 0, false);
			}
		}
		// else we did not receive either type, in which case there is an error and we return null
		return null;
	}
}
