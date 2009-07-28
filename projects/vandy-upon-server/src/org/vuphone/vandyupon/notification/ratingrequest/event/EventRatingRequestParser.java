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

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class EventRatingRequestParser implements NotificationParser {

	@Override
	public Notification parse(HttpServletRequest req) {
		
		if (!req.getParameter("type").equalsIgnoreCase("eventratingrequest"))
			return null;
		
		String response = req.getParameter("resp");
		String callback = req.getParameter("callback");
		long id = Long.parseLong(req.getParameter("id"));
		
		boolean getComments = Boolean.getBoolean(req.getParameter("com"));
		int numCom = 0;
		if (getComments){
			numCom = Integer.parseInt(req.getParameter("numcom"));
		}
		
		return new EventRatingRequest(response,callback, id, getComments, numCom);
		
	}

}
