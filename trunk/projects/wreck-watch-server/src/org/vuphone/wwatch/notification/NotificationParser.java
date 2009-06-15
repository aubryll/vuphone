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
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import org.vuphone.wwatch.accident.AccidentNotification;
import org.vuphone.wwatch.contacts.ContactUpdateNotification;
import org.vuphone.wwatch.inforeq.InfoNotification;
import org.vuphone.wwatch.routing.RouteNotification;
import org.vuphone.wwatch.media.ImageNotification;


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
				an.setLatitude(Double.parseDouble(req.getParameter("lat")));
				log_.log(Level.FINER, "Latitude: " + an.getLatitude());
				an.setLongitude(Double.parseDouble(req.getParameter("lon")));
				log_.log(Level.FINER, "Longitude: " + an.getLongitude());




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
					log_.log(Level.FINER, "Got exception handling InfoNotication: " + e.getMessage());
					n = null;
				}


			}else if (type.equalsIgnoreCase("contact")){
				String id = req.getParameter("id");
				n = new ContactUpdateNotification(id);
				ContactUpdateNotification cn = (ContactUpdateNotification)n;

				cn.setNumbers(req.getParameterValues("number"));

			}else if (type.equalsIgnoreCase("route")){

				n = new RouteNotification();
				RouteNotification rn = (RouteNotification)n;

				rn.setPerson(req.getParameter("id"));

				String[] lats = req.getParameterValues("lat");
				String[] lons = req.getParameterValues("lon");
				String[] times = req.getParameterValues("timert");

				if (lats != null){


					for(int i = 0; i < lats.length; ++i){
						if (lats[i] != null && lons[i] != null && times[i] != null){
							rn.addWaypoint(Double.parseDouble(lats[i]), Double.parseDouble(lons[i]), Long.parseLong(times[i]));
						}
					}
				}else{
					rn.addWaypoint(0, 0, System.currentTimeMillis());
				}
			} else if (type.equalsIgnoreCase("image")) {
				//handle image notications here.
			/*	
			  	if (req.getParameter("imageData") == null)
				{
					log_.log(Level.FINE, "No imageData found in image post");
					return n;
				}
				n = new ImageNotification();
				ImageNotification in = (ImageNotification) n;
				
				try {
					in.setBytes(req.getParameter("imageData").getBytes("UTF-8"));
				} catch (UnsupportedEncodingException uee) {
					log_.log(Level.FINE, "Unable to get Bytes out of request parameter: " + uee.getMessage());
				}
			*/
			java.util.Enumeration paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements())
			{
				String currentParamName = (String) paramNames.nextElement();
				String [] currentParamValues = req.getParameterValues(currentParamName);
				String currentParamValue = "";
				boolean first = true;
				for (String s: currentParamValues)
				{
					if (!first)
					{
						currentParamValue += ",";
					}
					currentParamValue += s;
					first = false;
				}
				log_.log(Level.SEVERE, "param name (" + currentParamName + ") value (" + currentParamValue + ")");
			}
			java.util.Enumeration headerNames = req.getHeaderNames();
			while (headerNames.hasMoreElements())
			{
				String currentHeaderName = (String) headerNames.nextElement();
				java.util.Enumeration currentHeaderValues = req.getHeaders(currentHeaderName);
				String currentHeaderValue = "";
				boolean first = true;
				while (currentHeaderValues.hasMoreElements())
				{
					String s = (String) currentHeaderValues.nextElement();
					if (!first)
					{
						currentHeaderValue += ",";
					}
					currentHeaderValue += s;
					first = false;
				}
				log_.log(Level.SEVERE, "param name (" + currentHeaderName + ") value (" + currentHeaderValue + ")");
			}
			}else{
				n = new Notification(type);
			}
		}
		return n;
	}
}
