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
package org.vuphone.wwatch.android.mapview;

import com.google.android.maps.GeoPoint;


public class EnhancedGeoPoint {
	
	
	private GeoPoint point_;
	private String notes_ = "";
	double lat_;
	double lon_;
	
	/**
	 * This constructor requires parameters in the form of microdegrees or 
	 * degrees * 1E6
	 * @param lat
	 * @param lon
	 */
	public EnhancedGeoPoint(int lat, int lon){
		point_ = new GeoPoint(lat, lon);
	}
	
	/**
	 * This constructor requires parameters in the form of microdegrees or 
	 * degrees * 1E6
	 * @param lat
	 * @param lon
	 */
	public EnhancedGeoPoint(int lat, int lon, String notes){
		point_ = new GeoPoint(lat, lon);
		notes_ = notes;
	}
	
	/**
	 * This constructor takes parameters in the form of degrees and 
	 * converts them to microdegrees
	 * @param lat
	 * @param lon
	 */
	public EnhancedGeoPoint(double lat, double lon){
		int latInt = (int) (lat * 1E6);
		int lonInt = (int) (lon * 1E6);
		point_ = new GeoPoint(latInt, lonInt);		
	}
	
	/**
	 * This constructor takes parameters in the form of degrees and 
	 * converts them to microdegrees
	 * @param lat
	 * @param lon
	 */
	public EnhancedGeoPoint(double lat, double lon, String notes){
		int latInt = (int) (lat * 1E6);
		int lonInt = (int) (lon * 1E6);
		point_ = new GeoPoint(latInt, lonInt);		
		notes_ = notes;
	}
	
	public EnhancedGeoPoint(){
		
	}
	
	public void setLat(double lat){
		lat_ = lat;
	}
	
	public void setLon(double lon){
		lon_ = lon;
	}
	
	public void createGeoPoint(){
		int latInt = (int) (lat_ * 1E6);
		int lonInt = (int) (lon_ * 1E6);
		point_ = new GeoPoint(latInt, lonInt);		
	}
	
	public GeoPoint getPoint(){
		return point_;
	}
	
	public void setNotes(String notes){
		notes_ = notes;
	}
	
	public String getNotes(){
		return notes_;
	}
	
	@Override
	public String toString(){
		return (point_.getLatitudeE6()/1E6) + ", " + (point_.getLongitudeE6()/1E6);
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EnhancedGeoPoint){
			EnhancedGeoPoint p = (EnhancedGeoPoint)o;
			GeoPoint temp = p.getPoint();
			return (temp.getLatitudeE6() == lat_) && (temp.getLongitudeE6() == lon_) && p.getNotes().equalsIgnoreCase(notes_);
		}else{
			return false;
		}
	}
	
	

}
