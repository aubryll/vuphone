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

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class EventMetaRequestParser implements NotificationParser {

	@Override
	public Notification parse(HttpServletRequest req) {
		
		if (!req.getParameter("type").equalsIgnoreCase("eventmetarequest"))
			return null;
		
		EventMetaRequest emr = new EventMetaRequest(Integer.parseInt(req.getParameter("id")), req.getParameter("resp"),
				req.getParameter("callback"));
		
		return emr;
	}

}
