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
import org.vuphone.wwatch.routing.RouteNotification;
import org.vuphone.wwatch.media.ImageNotification;

public class NotificationParser {

	private Logger log_ = Logger.getLogger(NotificationParser.class.getName());
	private HttpServletRequest request_;

	public Notification parse(HttpServletRequest req) {
		request_ = req;
		String type = req.getParameter("type");
		
		if (type == null)
			return null;

		if (type.equalsIgnoreCase("accident"))
			return handleAccident();
		else if (type.equalsIgnoreCase("info"))
			return handleInfo();
		else if (type.equalsIgnoreCase("contact"))
			return handleContact();
		else if (type.equalsIgnoreCase("route"))
			return handleRoute();
		else if (type.equalsIgnoreCase("image"))
			return handleImage();
		else
			return null;
	}

	private Notification handleAccident() {
		log_.log(Level.FINE, "Processing accident notification");
		Notification n = new AccidentNotification();
		AccidentNotification an = (AccidentNotification) n;
		an.setSpeed(Double.parseDouble(request_.getParameter("speed")));
		log_.log(Level.FINER, "Speed: " + an.getSpeed());
		an.setDeceleration(Double.parseDouble(request_.getParameter("dec")));
		log_.log(Level.FINER, "Acceleration: " + an.getDeceleration());
		an.setTime(Long.parseLong(request_.getParameter("time")));
		log_.log(Level.FINER, "Time: " + an.getTime());
		an.setParty(request_.getParameter("user"));
		log_.log(Level.FINER, "User: " + an.getPerson());
		an.setLatitude(Double.parseDouble(request_.getParameter("lat")));
		log_.log(Level.FINER, "Latitude: " + an.getLatitude());
		an.setLongitude(Double.parseDouble(request_.getParameter("lon")));
		log_.log(Level.FINER, "Longitude: " + an.getLongitude());

		return n;
	}

	private Notification handleInfo() {
		Notification n = new InfoNotification();
		InfoNotification info = (InfoNotification) n;

		int latE6;
		double lat;

		int lonE6;
		double lon;
		try {

			latE6 = Integer.parseInt(request_.getParameter("lattl"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request_.getParameter("lontl"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setTopLeftCorner(lat, lon);

			latE6 = Integer.parseInt(request_.getParameter("lattr"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request_.getParameter("lontr"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setTopRightCorner(lat, lon);

			latE6 = Integer.parseInt(request_.getParameter("latbl"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request_.getParameter("lonbl"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setBottomLeftCorner(lat, lon);

			latE6 = Integer.parseInt(request_.getParameter("latbr"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request_.getParameter("lonbr"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setBottomRightCorner(lat, lon);

		} catch (Exception e) {
			// If we get here, likely the parameters were wrong
			n = null;
		}

		return n;
	}

	private Notification handleContact() {
		String id = request_.getParameter("id");
		Notification n = new ContactUpdateNotification(id);
		ContactUpdateNotification cn = (ContactUpdateNotification) n;

		cn.setNumbers(request_.getParameterValues("number"));
		return n;
	}


	private Notification handleRoute() {
		Notification n = new RouteNotification();
		RouteNotification rn = (RouteNotification) n;

		rn.setPerson(request_.getParameter("id"));

		String[] lats = request_.getParameterValues("lat");
		String[] lons = request_.getParameterValues("lon");
		String[] times = request_.getParameterValues("timert");

		if (lats != null) {

			for (int i = 0; i < lats.length; ++i) {
				if (lats[i] != null && lons[i] != null && times[i] != null) {
					rn.addWaypoint(Double.parseDouble(lats[i]), Double
							.parseDouble(lons[i]), Long.parseLong(times[i]));
				}
			}
		} else {
			rn.addWaypoint(0, 0, System.currentTimeMillis());
		}

		return n;
	}
	
	private Notification handleImage() {
		Notification n = new ImageNotification();
		ImageNotification in = (ImageNotification) n;
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
		java.util.Enumeration paramNames = request_.getParameterNames();
		while (paramNames.hasMoreElements())
		{
			String currentParamName = (String) paramNames.nextElement();
			String [] currentParamValues = request_.getParameterValues(currentParamName);
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
		java.util.Enumeration headerNames = request_.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String currentHeaderName = (String) headerNames.nextElement();
			java.util.Enumeration currentHeaderValues = request_.getHeaders(currentHeaderName);
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
		return in;
	}
}
