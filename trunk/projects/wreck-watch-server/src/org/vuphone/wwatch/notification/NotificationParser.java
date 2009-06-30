/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.notification;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.media.incoming.ImageNotification;

public class NotificationParser {

	private Logger log_ = Logger.getLogger(NotificationParser.class.getName());
	private HttpServletRequest request_;

	public Notification parse(HttpServletRequest req, HttpServletResponse resp) {

		String type = req.getParameter("type");
	
		if (type == null)
			return null;

		Notification n = new Notification(type);
		n.setRequest(req);
		n.setResponse(resp);
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
		/*java.util.Enumeration paramNames = request_.getParameterNames();
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
		}*/
		return in;
	}
	
}