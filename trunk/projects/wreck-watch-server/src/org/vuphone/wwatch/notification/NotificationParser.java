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

import org.vuphone.wwatch.accident.AccidentNotification;
import org.vuphone.wwatch.contacts.ContactUpdateNotification;
import org.vuphone.wwatch.inforeq.InfoNotification;


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
				an.setParty(req.getParameter("user"));
				log_.log(Level.FINER, "User: " + an.getPerson());

				Integer numPoints = Integer.parseInt(req.getParameter("numpoints"));

				if (numPoints != null){
					for (int i = 0; i < numPoints; ++i){
						an.addWaypoint(Double.parseDouble(req.getParameter("lat" + i)), Double.parseDouble(req.getParameter("lon" + i)), 
								Long.parseLong(req.getParameter("time" + i)));
					}
				}
				an.setAccidentLocation();
			}else if (type.equalsIgnoreCase("info")){

				n = new InfoNotification();
				InfoNotification info = (InfoNotification)n;

				int latE6;
				double lat;

				int lonE6;
				double lon;
				try{

					latE6 = Integer.parseInt(req.getParameter("lattl"));
					lat = (double)latE6;
					lat = lat /1E6;
					lonE6 = Integer.parseInt(req.getParameter("lontl"));
					lon = (double)lonE6;
					lon = lon / 1E6;
					info.setTopLeftCorner(lat, lon);

					latE6 = Integer.parseInt(req.getParameter("lattr"));
					lat = (double)latE6;
					lat = lat /1E6;
					lonE6 = Integer.parseInt(req.getParameter("lontr"));
					lon = (double)lonE6;
					lon = lon / 1E6;
					info.setTopRightCorner(lat, lon);

					latE6 = Integer.parseInt(req.getParameter("latbl"));
					lat = (double)latE6;
					lat = lat /1E6;
					lonE6 = Integer.parseInt(req.getParameter("lonbl"));
					lon = (double)lonE6;
					lon = lon / 1E6;
					info.setBottomLeftCorner(lat, lon);

					latE6 = Integer.parseInt(req.getParameter("latbr"));
					lat = (double)latE6;
					lat = lat /1E6;
					lonE6 = Integer.parseInt(req.getParameter("lonbr"));
					lon = (double)lonE6;
					lon = lon / 1E6;
					info.setBottomRightCorner(lat, lon);
					
					
				}catch (Exception e) {
					//If we get here, likely the parameters were wrong
					n = null;
				}
				

			}else if (type.equalsIgnoreCase("contact")){
				String id = req.getParameter("id");
				n = new ContactUpdateNotification(id);
				ContactUpdateNotification cn = (ContactUpdateNotification)n;
				
				Integer num = Integer.parseInt(req.getParameter("numcontacts"));
				
				for (int i = 0; i < num; ++i){
					cn.addNumber(req.getParameter("number"+i));
				}
				
			}else{
				n = new Notification(type);
			}
		}
		return n;
	}
}
