/**
 * @author Michael Fagan                                            
 *                      
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
 * This class will be used to hold a Notification as it pertains to a Dining Rating.
 */

package org.vuphone.vandyupon.notification.diningrating;

import org.vuphone.vandyupon.notification.Notification;

public class DiningRating extends Notification{

	// This variable is for the integer mapped to the proper location
	private int loc_;
	// This variable is used to hold the rating to be added to the Database
	private int rating_;
	// This variable is used to hold the ID of the device
	private String deviceID_;
	
	/**
	 * The default Ctor
	 */
	public DiningRating(){
		super("diningrating");
	}
	
	/**
	 * Ctor for a Rating Addition
	 *  
	 * @param location The int representing the location being looked up
	 * @param rating The rating being added
	 * @param deviceID The ID of the device making the request
	 */
	public DiningRating(int location, int rating, String deviceID){
		super("diningrating");
		loc_ = location;
		rating_ = rating;
		deviceID_ = deviceID;
	}
	
	/**
	 * Returns the location in the container
	 *  
	 * @return The location being requested
	 */
	public int getLocation(){
		return loc_;
	}
	
	/**
	 * Returns the rating in the container
	 *  
	 * @return The rating being added
	 */
	public int getRating(){
		return rating_;
	}
	
	/**
	 * Returns the deviceID in the container
	 *  
	 * @return The deviceID that made the request
	 */
	public String getDeviceID(){
		return deviceID_;
	}
	
	/**
	 * Sets the location being requested
	 *  
	 * @param location The location to be set
	 */
	public void setLocation(int location){
		loc_ = location;
	}
	
	/**
	 * Sets the location being requested
	 *  
	 * @param rating The rating to be added
	 */
	public void setRating(int rating){
		rating_ = rating;
	}
	
	/**
	 * Sets the deviceID making the request
	 *  
	 * @param deviceID The ID of the device making the request
	 */
	public void setDeviceID(String deviceID){
		deviceID_ = deviceID;
	}
	
}
