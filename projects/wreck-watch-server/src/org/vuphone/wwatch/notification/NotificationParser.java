/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.notification;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


public class NotificationParser {
	
	private Logger log_ = Logger.getLogger(NotificationParser.class.getName());

	public Notification parse(HttpServletRequest req)
			throws NotificationFormatException {
		String type = req.getParameter("type");
		
		Notification n = null;

		if (type != null){
			if (type.equals("accident")){
				log_.log(Level.FINE, "Processing accident notification");
				n = new AccidentNotification();
				AccidentNotification an = (AccidentNotification)n;
				an.setSpeed(Double.parseDouble(req.getParameter("speed")));
				log_.log(Level.FINER, "Speed: " + an.getSpeed());
				an.setDeceleration(Double.parseDouble(req.getParameter("dec")));
				log_.log(Level.FINER, "Acceleration: " + an.getDeceleration());
				an.setTime(Long.parseLong(req.getParameter("time")));
				log_.log(Level.FINER, "Time: " + an.getTime());
				
				Integer numPoints = Integer.parseInt(req.getParameter("numpoints"));
				
				if (numPoints != null){
					for (int i = 0; i < numPoints; ++i){
						an.addWaypoint(Double.parseDouble(req.getParameter("lat")), Double.parseDouble(req.getParameter("lon")), 
								Long.parseLong(req.getParameter("time")));
					}
				}
			}else if (type.equalsIgnoreCase("info")){
				int latE6 = Integer.parseInt(req.getParameter("lat"));
				double lat = (double)latE6;
				int lonE6 = Integer.parseInt(req.getParameter("lon"));
				double lon = (double)lonE6;
				
				
			}else{
			
				n = new Notification(type);
			}
		}
		return n;
	}
}
