/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
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