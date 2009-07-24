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
package org.vuphone.vandyupon.notification.eventrequest;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;

public class EventRequest extends Notification {
	
	private Location anchorPt_;
	private double distance_;
	private String callback_;
	private String responseType_;
	private String userid_;
	private long updatetime_;
	
	public EventRequest(Location anchor, double distance, 
			String userid, String responseType, String callback, long updatetime){
		super("eventrequest");
		anchorPt_ = anchor;
		distance_ = distance;
		responseType_ = responseType;
		userid_ = userid;
		callback_ = callback;
		updatetime_ = updatetime;
	}
	
	public double getDistance(){
		return distance_;
	}
	
	public Location getAnchor(){
		return anchorPt_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public long getUpdateTime(){
		return updatetime_;
	}
	
	public String getUserId(){
		return userid_;
	}

	public void setUserId(String userid){
		userid_ = userid;
	}
}
