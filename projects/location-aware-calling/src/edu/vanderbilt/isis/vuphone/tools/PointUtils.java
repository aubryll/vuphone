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
package edu.vanderbilt.isis.vuphone.tools;

import com.google.android.maps.GeoPoint;

/**
 * This class is designed as a helper class of utilities for point conversion
 * and calculation.  Parts of this class were adapted from a post by Somar at
 * http://board.flashkit.com/board/archive/index.php/t-666832.html
 * 
 * 
 * @author Chris Thompson
 *
 */

public class PointUtils {
	
	//This value was taken from a suggestion at the above post
	//where it was suggested to be a constant value between 150 
	//and 200.  However, given the radius of the Earth, it may
	//need to be much larger.  For testing purposes, it will be 
	//extracted as a constant.  
	private static final double FOCAL_LENGTH = 175;
	
	public static PrecisionPoint convertGeoPointToXY(GeoPoint point){
		
		double lat = point.getLatitudeE6() / 1E6;		
		double lon = point.getLongitudeE6() / 1E6;
		
		return convertDegreesToXY(lat, lon);
	}
	
	public static PrecisionPoint convertDegreesToXY(double lat, double lon){
		
		//Convert them to radians
		lat = toRads(lat);
		lon = toRads(lon);
		
		return convertRadiansToXY(lat, lon);
	}
	
	public static PrecisionPoint convertRadiansToXY(double lat, double lon){

		//Perform the Cartesian conversion
		double x = 6378100 * Math.cos(lat) * Math.cos(lon);
		double y = 6378100 * Math.cos(lat) * Math.sin(lon);
		double z = 6378100 * Math.sin(lat);
		
		//Perform the projection to convert from (x,y,x) coords
		//to (x,y) coords.
		x = x * FOCAL_LENGTH / (FOCAL_LENGTH + z);
		y = y * FOCAL_LENGTH / (FOCAL_LENGTH + z);
		
		return new PrecisionPoint(x, y);
	}
	
	/**
	 * Private helper function to convert degress to radians
	 * @param degrees
	 * @return
	 */
	private static double toRads(double degrees){
		return Math.PI * degrees / 180;
	}

}
