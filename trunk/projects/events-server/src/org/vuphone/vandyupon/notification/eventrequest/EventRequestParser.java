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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class EventRequestParser implements NotificationParser {

	/**
	 * This method is used to parse an event request. The Http parameters it
	 * expects are the anchor point and the radius from that anchor point.
	 * 
	 */
	public Notification parse(HttpServletRequest req) {
		Location loc = new Location(
				Double.parseDouble(req.getParameter("lat")), Double
						.parseDouble(req.getParameter("lon")));
		double distance = Double.parseDouble(req.getParameter("dist"));
		String response = req.getParameter("resp");
		String callback = req.getParameter("callback");
		String userid = req.getParameter("userid");
		long update = Long.parseLong(req.getParameter("updatetime"));
		long startTime = 0, endTime = Long.MAX_VALUE;
		if (req.getParameter("starttime") != null)
			startTime = Long.parseLong(req.getParameter("starttime"));
		if (req.getParameter("endtime") != null)
			endTime = Long.parseLong(req.getParameter("endtime"));
		
		List<String> tags = new ArrayList<String>();
		if (req.getParameter("tags") != null)
		{
			String tagsCsv = req.getParameter("tags");
			for (String s: tagsCsv.split(","))
				tags.add(s);
		}
		
		return new EventRequest(loc, distance, userid, response, callback,
				update, startTime, endTime, tags);
	}

}
