/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.notification;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificationParser {

	private Logger log_ = Logger.getLogger(NotificationParser.class.getName());

	public Notification parse(HttpServletRequest req, HttpServletResponse resp) {

		String type = req.getParameter("type");
	
		log_.log(Level.FINER, "type was "+type);
		
		if (type == null)
			return null;

		Notification n = new Notification(type);
		n.setRequest(req);
		n.setResponse(resp);
		return n;
	}
	
}