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

import java.util.ArrayList;

import org.vuphone.vandyupon.datastructs.Event;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventRequestResponse extends ResponseNotification {
	
	private ArrayList<Event> events_;
	
	public EventRequestResponse(){
		super("eventrequestresponse");
		events_ = new ArrayList<Event>();
	}
	
	public void addEvent(Event e){
		events_.add(e);
	}
	
	public ArrayList<Event> getEvents(){
		return events_;
	}

}
