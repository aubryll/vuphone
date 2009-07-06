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
package org.vuphone.wwatch.inforeq;

public class Wreck {
	
	private double lat_;
	private double lon_;
	private int id_;
	private long time_;
	
	public Wreck(){
		
	}
	
	public Wreck(double lat, double lon, int id, long time){
		lat_ = lat;
		lon_ = lon;
		id_ = id;
		time_ = time;
	}
	
	public void setLat(double lat){
		lat_ = lat;
	}
	
	public void setLon(double lon){
		lon_ = lon;
	}
	
	public void setId(int id){
		id_ = id;
	}
	
	public void setTime(long time){
		time_ = time;
	}
	
	public double getLat(){
		return lat_;
	}
	
	public double getLon(){
		return lon_;
	}
	
	public long getTime(){
		return time_;
	}
	
	public int getId(){
		return id_;
	}

}
