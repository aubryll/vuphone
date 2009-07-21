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
package org.vuphone.vandyupon.notification.eventmetarequest;

import org.vuphone.vandyupon.notification.Notification;

public class EventMetaRequest extends Notification {

	private int event_;
	private String responseType_;
	private String callback_;
	
	public EventMetaRequest(int eventid, String responsetype, String callback){
		super("eventmeta");
		event_ = eventid;
		responseType_ = responsetype;
		callback_ = callback;
	}
	
	public int getEvent(){
		return event_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public String getCallback(){
		return callback_;
	}
}
