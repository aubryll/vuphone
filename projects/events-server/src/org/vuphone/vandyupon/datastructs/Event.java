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
package org.vuphone.vandyupon.datastructs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Event {
	
	private String name_;
	private Location loc_;
	private boolean owner_;
	private long startTime_;
	private long endTime_;
	private String sourceUid_;
	private int id_;
	private long lastUpdate_;
	private String description_;
	
	public Event(){
		
	}
	
	public Event(String name, Location loc, boolean owner, long startTime, long endTime, int id, long lastUpdate){
		Logger logger = Logger.getLogger("org.vuphone.vandyupon.datastructs"); 

		try {
			name_ = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.WARNING, "UTF-8 not supported. It should be. ");
			e.printStackTrace();
			name_ = name;
		}
		
		loc_ = loc;
		owner_ = owner;
		startTime_ = startTime;
		endTime_ = endTime;
		id_ = id;
		lastUpdate_ = lastUpdate;
	}
	
	public long getEndTime(){
		return endTime_;
	}
	
	public int getID(){
		return id_;
	}
	
	public long getLastUpdate(){
		return lastUpdate_;
	}
	
	public Location getLocation(){
		return loc_;
	}
	
	public String getName(){
		return name_;
	}
	
	public long getStartTime(){
		return startTime_;
	}
	
	public String getSourceUid() {
		return sourceUid_;
	}
	
	public String getDescription() {
		return "<![CDATA[" + description_ + "]]>";
	}
	
	public boolean isOwner(){
		return owner_;
	}
	
	public void setEndTime(long endTime){
		endTime_ = endTime;
	}
	
	public void setID(int id){
		id_ = id;
	}
	
	public void setLastUpdate(long lastUpdate){
		lastUpdate_ = lastUpdate;
	}
	
	public void setLocation(Location loc){
		loc_ = loc;
	}
	
	public void setName(String name){
		try {
			name_ = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger logger = Logger.getLogger("org.vuphone.vandyupon.datastructs"); 
			logger.log(Level.WARNING, "UTF-8 not supported. It should be. ");
			e.printStackTrace();
			name_ = name;
		}
	}
	
	public void setStartTime(long startTime){
		startTime_ = startTime;
	}
	
	public void setIsOwner(boolean owner){
		owner_ = owner;
	}

	public void setSourceUid(String sourceUid) {
		sourceUid_ = sourceUid;
	}

	public void setDescription(String description) {
		description_ = description;
	}
}
