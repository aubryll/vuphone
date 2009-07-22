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

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class EventRequestParser implements NotificationParser {
	
	
	/**
	 * This method is used to parse an event request.  The Http parameters
	 * it expects are the anchor point and the radius from that anchor point.
	 * 
	 */
	public Notification parse(HttpServletRequest req) {
		Location loc = new Location(Double.parseDouble(req.getParameter("lat")), Double.parseDouble(req.getParameter("lon")));
		double distance = Double.parseDouble(req.getParameter("dist"));
		String response = req.getParameter("resp");
		String callback = req.getParameter("callback");
		String userid = req.getParameter("userid");
		return new EventRequest(loc, distance, userid, response, callback);
	}
	

}
