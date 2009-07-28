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
package org.vuphone.vandyupon.notification.ratingrequest.event;

import org.vuphone.vandyupon.notification.Notification;

public class EventRatingRequest extends Notification{
	
	private String response_;
	private String callback_;
	private long id_;
	private boolean returnComments_;
	private int numComments_;
	
	public EventRatingRequest(String response, String callback, 
			long id, boolean pullComments, int numComments){
		super("eventratingrequest");
		response_ = response;
		callback_ = callback;
		id_ = id;
		returnComments_ = pullComments;
		numComments_ = numComments;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public long getId(){
		return id_;
	}
	
	public int getNumComments(){
		return numComments_;
	}
	
	public String getResponseType(){
		return response_;
	}
	
	public boolean shouldPullComments(){
		return returnComments_;
	}

}
