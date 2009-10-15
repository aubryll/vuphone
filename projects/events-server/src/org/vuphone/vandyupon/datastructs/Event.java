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


public class Event {
	
	private String name_;
	private Location loc_;
	private boolean owner_;
	private long startTime_;
	private long endTime_;
	private int id_;
	private long lastUpdate_;
	
	
	public Event(){
		
	}
	
	public Event(String name, Location loc, boolean owner, long startTime, long endTime, int id, long lastUpdate){
		name_ = name;
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
		name_ = name;
	}
	
	public void setStartTime(long startTime){
		startTime_ = startTime;
	}
	
	public void setIsOwner(boolean owner){
		owner_ = owner;
	}
}
