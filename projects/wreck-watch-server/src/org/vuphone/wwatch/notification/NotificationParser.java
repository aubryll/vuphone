/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.notification;

import javax.servlet.http.HttpServletRequest;


public class NotificationParser {

	public Notification parse(HttpServletRequest req)
			throws NotificationFormatException {
		String type = req.getParameter("type");
		
		Notification n = null;

		if (type != null){
			if (type.equals("accident")){
				n = new AccidentNotification();
				AccidentNotification an = (AccidentNotification)n;
				an.setSpeed(Double.parseDouble(req.getParameter("speed")));
				an.setDeceleration(Double.parseDouble(req.getParameter("dec")));
				an.setTime(Long.parseLong(req.getParameter("time")));
				
				Integer numPoints = Integer.parseInt(req.getParameter("numpoints"));
				
				if (numPoints != null){
					for (int i = 0; i < numPoints; ++i){
						an.addWaypoint(Double.parseDouble(req.getParameter("lat")), Double.parseDouble(req.getParameter("lon")), 
								Long.parseLong(req.getParameter("time")));
					}
				}
			}else {
				n = new Notification(type);
			}
		}
		return n;
	}
}
