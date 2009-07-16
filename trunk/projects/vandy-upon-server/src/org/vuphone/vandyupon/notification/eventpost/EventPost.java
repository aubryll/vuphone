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
package org.vuphone.vandyupon.notification.eventpost;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;

public class EventPost extends Notification {
	
	private Location loc_;
	private String name_;
	private int user_;
	private long start_;
	private long end_;
	private String callback_;
	private String responseType_;
	
	
	public EventPost(){
		super("eventpost");
	}
	
	public EventPost(Location loc, String name, int user, long start, long end, 
			String callback, String responseType){
		super("eventpost");
		loc_ = loc;
		name_ = name;
		user_ = user;
		start_ = start;
		end_ = end;
		callback_ = callback;
		responseType_ = responseType;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public long getEndTime(){
		return end_;
	}

	/**
	 * @return the loc_
	 */
	public Location getLocation() {
		return loc_;
	}

	/**
	 * @return the name_
	 */
	public String getName() {
		return name_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public long getStartTime(){
		return start_;
	}

	/**
	 * @return the user_
	 */
	public int getUser() {
		return user_;
	}
	
	public void setCallback(String cb){
		callback_ = cb;
	}
	
	public void setEndTime(long end){
		end_ = end;
	}
	/**
	 * @param loc the loc_ to set
	 */
	public void setLocation(Location loc) {
		loc_ = loc;
	}

	/**
	 * @param name the name_ to set
	 */
	public void setName(String name) {
		name_ = name;
	}
	
	public void setReponseType(String type){
		responseType_ = type;
	}
	
	public void setStartTime(long start){
		start_ = start;
	}

	/**
	 * @param user the user_ to set
	 */
	public void setUser(int user) {
		user_ = user;
	}
	
	
	
	

}
