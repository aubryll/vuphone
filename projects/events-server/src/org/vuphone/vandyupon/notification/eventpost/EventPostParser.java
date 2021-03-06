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
package org.vuphone.vandyupon.notification.eventpost;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class EventPostParser implements NotificationParser {

	public Notification parse(HttpServletRequest req) {
		// Use UTF-8 encoding
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (!req.getParameter("type").equalsIgnoreCase("eventpost")) {
			return null;
		}

		EventPost ep = new EventPost();
		ep.setLocationName(req.getParameter("locationname"));
		if (req.getParameter("locationlat") == null
				|| req.getParameter("locationlon") == null) {
			ep.setLocation(null);
		} else {
			ep.setLocation(new Location(Double.parseDouble(req
					.getParameter("locationlat")), Double.parseDouble(req
					.getParameter("locationlon"))));
		}

		ep.setName(req.getParameter("eventname"));
		ep.setStartTime(Long.parseLong(req.getParameter("starttime")));
		ep.setEndTime(Long.parseLong(req.getParameter("endtime")));
		ep.setUser(req.getParameter("userid"));
		ep.setReponseType(req.getParameter("resp"));
		ep.setCallback(req.getParameter("callback"));
		try {
			if (req.getParameter("desc") != null)
				ep.setDescription(URLDecoder.decode(req.getParameter("desc"),
						"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("Description is now: " + ep.getDescription());
		ep.setSourceUid(req.getParameter("sourceuid"));

		if (req.getParameter("tags") != null) {
			String[] tagArray = req.getParameter("tags").split(",");
			ArrayList<String> tags = new ArrayList<String>();
			for (String s : tagArray)
				tags.add(s);
			ep.setTags(tags);
		}

		return ep;
	}

}
