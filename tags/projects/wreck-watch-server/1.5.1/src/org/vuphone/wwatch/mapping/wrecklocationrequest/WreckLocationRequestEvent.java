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
package org.vuphone.wwatch.mapping.wrecklocationrequest;

import org.vuphone.wwatch.mapping.MapEvent;

public class WreckLocationRequestEvent extends MapEvent {
	
	private double[][] corners;
	
	public WreckLocationRequestEvent(){
		super("locationrequest");
		corners = new double[2][2];
	}
	
	public void setNorthEastLat(double nelat){
		corners[1][0] = nelat;
	}
	public void setNorthEastLon(double nelon){
		corners[1][1] = nelon;
	}
	
	public void setSouthWestLat(double swlat){
		corners[0][0] = swlat;
	}
	public void setSouthWestLon(double swlon){
		corners[0][1] = swlon;
	}
	
	public double getNorthEastLat(){
		return corners[1][0];
	}
	public double getNorthEastLon(){
		return corners[1][1];
	}
	
	public double getSouthWestLat(){
		return corners[0][0];
	}
	public double getSouthWestLon(){
		return corners[0][1];
	}

}
