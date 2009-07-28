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
package org.vuphone.vandyupon.notification.ratingpost.event;

import org.vuphone.vandyupon.notification.Notification;

public class EventRatingPost extends Notification {
	
	private String userId_;
	private String comment_;
	private String value_;
	private long eventId_;
	private String responseType_;
	private String callback_;
	
	public EventRatingPost(String user, long event, String comment, 
			String value, String responseType, String callback){
		super("eventratingpost");
		userId_ = user;
		comment_ = comment;
		value_ = value;
		eventId_ = event;
		responseType_ = responseType;
		callback_ = callback;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public String getComment(){
		return comment_;
	}
	
	public long getEvent(){
		return eventId_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public String getValue(){
		return value_;
	}
	
	public String getUser(){
		return userId_;
	}

}
