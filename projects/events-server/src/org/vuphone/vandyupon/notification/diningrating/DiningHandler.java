/**
 * @author Michael Fagan                                            
 * @date 4/8/10                     
 * @section LICENCE
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 * http://www.apache.org/licenses/LICENSE-2.0                              
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License. 
 * 
 * @section DESCRIPTION
 * 
 * @class DiningHandler
 * @brief This class handles the two types of DiningRatings, either DiningRating or DiningRatingRequest.
 * It performs the necessary operations on the Database and returns the appropriate Response type
 * for each call.
 */

package org.vuphone.vandyupon.notification.diningrating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class DiningHandler implements NotificationHandler {
	
	
	// The datasource, aka the Database connection
	private DataSource ds_;
	
	/** 
	 * This method gets the SQL connection.
	 * 
	 * @return ds_ A datasource
	 */
	public DataSource getDataConnection() {
		return ds_;
	}
	
	/**
	 * This method sets the datasource
	 * 
	 * @param ds A datasource
	 */
	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}
	
	/**
	 * This method adds a rating to the database.
	 *  
	 * @param dp A container that has the data for a rating to be added
	 * @return 1 on success
	 * @throws SQLException Something wrong with the SQL query
	 */
	private int addRating(DiningRating dp) throws SQLException {
		if((checkLocation(dp.getLocation()))&&(checkRating(dp.getRating()))){
			String sql;
			Connection conn = ds_.getConnection();
			
			// Inserts a rating into the DiningRatings Table
			sql = "INSERT INTO DiningRatings (deviceid, loc, rating) VALUES (?, ?, ?)";
			
			// This block puts the passed values into the above statement
			PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, dp.getDeviceID());
			prep.setInt(2, dp.getLocation());
			prep.setInt(3, dp.getRating());
	
			// Executes the statement and checks for failure
			if (prep.executeUpdate() == 0) {
				throw new SQLException(
						"Insertion into vandyupon.dining failed for an unknown reason");
			}
			return 1;
		}
		else
		{
			//Generic exception. Should be replaced with something more descriptive later
			throw new SQLException();
		}
	}
	
	/**
	 * This method updates the rating of a Location from a Device that has already sent a rating
	 * 
	 * @param dp A container that has the data for a rating to be updated
	 * @return 1 on success
	 * @throws SQLException Something wrong with the SQL query
	 */
	private int updateRating(DiningRating dp) throws SQLException {
		if((checkLocation(dp.getLocation()))&&(checkRating(dp.getRating()))){
			String sql;
			Connection conn = ds_.getConnection();
			
			// Updates the rating
			sql = "UPDATE DiningRatings SET rating= ? WHERE DeviceID= ? AND loc = ?";
			
			// This block puts the passed in values into the SQL statement
			PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, dp.getRating());
			prep.setString(2, dp.getDeviceID());
			prep.setInt(3, dp.getLocation());
	
			// Executes the statement and checks for failure
			if (prep.executeUpdate() == 0) {
				throw new SQLException(
						"Insertion into vandyupon.dining failed for an unknown reason");
			}
			return 1;
		}
		else
		{
			//Generic exception. Should be replaced with something more descriptive later
			throw new SQLException();
		}
		
	}
	
	/**
	 * This method is used to see if the request is already in the Database
	 * 
	 * @param dp A container that has the data for a rating to be updated
	 * @return true If the entry is in the database, false If it is not
	 * @throws SQLException Something wrong with the SQL query
	 */
	private boolean checkForEntry(DiningRating dp) throws SQLException {
		Connection conn = ds_.getConnection();
		String sql;
		
		// The statement tries to select from the DiningRatings table to see if an entry exists
		sql = "SELECT * FROM DiningRatings WHERE DeviceID = ? AND loc = ?";
		
		// This block places the passed values into the above statement
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, dp.getDeviceID());
		prep.setInt(2, dp.getLocation());
		
		// Execute the statement
		ResultSet rs = prep.executeQuery();
		
		// Tests to see if anything was returened from the SQL query
		try{
			if(rs.next())
				return true;
			else/* The ID is not in the database with the current location */ 
			{
				rs.close();
				return false;
			}
		} catch (SQLException e) {
			System.out.print("SQL Exception in checking entry\n");
			return false;
		}
	}
	
	/**
	 * This method performs the SQL to get the rating for a particular location.
	 * 
	 * @param drr A container that contains the information to get the rating for the requested location
	 * @see DiningRatingRequest
	 * @return The average rating for the location if successful
	 * @throws SQLException Something wrong with the SQL query
	 */
	private int getRating(DiningRatingRequest drr) throws SQLException {
		
		if(checkLocation(drr.getLocation())){
			String sql;
			Connection conn = ds_.getConnection();
			
			// The statement returns the average of the ratings for a particular location
			sql = "SELECT AVG(rating) FROM DiningRatings WHERE loc = ?";
			
			// The block places the passed values into the above statement
			PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, drr.getLocation());
			
			// Executes and returns
			ResultSet rs = prep.executeQuery();
			rs.next();
			return rs.getInt(1);
		}
		else
		{
			//Generic exception. Should be replaced with something more descriptive later
			throw new SQLException();
		}
	}
	
	// The method validates the location.
	private boolean checkLocation(int loc){
		return ((loc>0)&&(loc<=47));
	}
	
	// This method validates the rating.
	private boolean checkRating(int rat){
		return ((rat>0)&&(rat<=5));
	}
	
	/**
	 *  This method actually handles the DiningRatings and DiningRatingRequests.
	 *  We perform the proper functions based on the type we receive.  After that,
	 *  we return the proper response type.  If we do not receive a type we know 
	 *  how to deal with, throws an illegal argument exception
	 *  
	 *  @param Notification A generic notification
	 *  @see Notification
	 *  @return A DiningRatingResponse if the request was to add or change a rating, 
	 *  @return A DiningRatingRequestResponse if the request was for a rating for a location
	 */

	public ResponseNotification handle(Notification n){
		if(n instanceof DiningRating)// The type is DiningRating
		{
			
			// Cast the Generic Notification as DiningRating
			DiningRating dr = (DiningRating) n;
			
			try{
				if(!(checkForEntry(dr))){
					
					// Add the rating
					addRating(dr);
					return new DiningRatingResponse("not important", null, true);
					// Must put something for ResponseType.. the responseHandler will call it.
				}
				else
				{
					// Update the rating
					updateRating(dr);
					return new DiningRatingResponse("not important", null, true);
					// Must put something for ResponseType.. the responseHandler will call it.
				}
			}
			catch(SQLException e){
				System.out.print("SQL fail");
				return new DiningRatingResponse("not important", null, false);
			}
		}
		else if(n instanceof DiningRatingRequest)// The type is DiningRatingRequest
		{
			
			// Cast the generic notification as DiningRatingRequet
			DiningRatingRequest drr = (DiningRatingRequest) n;
			
			try{
				
				// Get the average rating.
				int avgRating = getRating(drr);
				return new DiningRatingRequestResponse("not important", null, avgRating, true);
			}
			catch(SQLException e){
				return new DiningRatingRequestResponse("not important", null, 0, false);
			}
		}
		else // we did not receive either type, in which case there is another error
		{
			return new DiningRatingResponse("not important", null, false);
		}
	}
}
