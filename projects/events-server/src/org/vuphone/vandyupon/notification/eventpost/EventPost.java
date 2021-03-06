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

import java.util.List;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;

public class EventPost extends Notification {
	
	private String locationName_;
	private Location loc_;
	private String name_;
	private String user_;
	private long start_;
	private long end_;
	private String sourceUid_;
	private String callback_;
	private String responseType_;
	private String description_;
	private int dbUserId_;
	private List<String> tags_;
	
	public EventPost(){
		super("eventpost");
	}
	
	public EventPost(Location loc, String name, String user, long start, long end, 
			String callback, String responseType) {
		this(loc, name, user, start, end, callback, responseType, null);
	}
	
	public EventPost(Location loc, String name, String user, long start, long end, 
			String callback, String responseType, List<String> tags){
		super("eventpost");
		loc_ = loc;
		name_ = name;
		if (name_ == null)
			name_ = "";
		user_ = user;
		start_ = start;
		end_ = end;
		callback_ = callback;
		responseType_ = responseType;
		tags_ = tags;
	}
	
	public void setTags(List<String> tags)
	{
		tags_ = tags;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public List<String> getTags(){
		return tags_;
	}
	
	public String getDescription() {
		return description_;
	}

	public long getEndTime(){
		return end_;
	}

	public Location getLocation() {
		return loc_;
	}

	public String getName() {
		return name_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public long getStartTime(){
		return start_;
	}

	public String getUser() {
		return user_;
	}
	
	public void setCallback(String cb){
		callback_ = cb;
	}
	
	public void setDescription(String description) {
		description_ = description;
	}
	
	public void setEndTime(long end){
		end_ = end;
		
	}

	public void setLocation(Location loc) {
		loc_ = loc;
	}

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
	public void setUser(String user) {
		user_ = user;
	}
	
	public void setDbUserId(int user){
		dbUserId_ = user;
	}
	
	public int getDbUserId(){
		return dbUserId_;
	}

	public void setLocationName(String locationName) {
		locationName_ = locationName;
	}
	
	public String getLocationName() {
		return locationName_;
	}

	public void setSourceUid(String sourceUid) {
		sourceUid_ = sourceUid;
	}

	public String getSourceUid() {
		return sourceUid_;
	}

}
