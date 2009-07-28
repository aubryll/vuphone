/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
package org.vuphone.vandyupon.media.incoming.event;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;


public class ImageParser implements NotificationParser {
	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private static final String TIME = "time";
	private static final String EVENTID = "eventid";
	private static final String CONTENT_TYPE = "image/jpeg";
	
	public boolean isRequestValid(HttpServletRequest request)
	{
		boolean isValid = false;

		if (request.getParameter(ImageParser.TIME) == null)
		{
			logger_.log(Level.SEVERE, "Unable to get time from the request");
		}
		else if (request.getContentType() == null ||
				!request.getContentType().equalsIgnoreCase(ImageParser.CONTENT_TYPE))
		{
			logger_.log(Level.SEVERE, "Expected Content Type to be " + ImageParser.CONTENT_TYPE + " not " + request.getContentType());
		}
		else if (request.getContentLength() <= 0)
		{
			logger_.log(Level.SEVERE, "Expected Content Length to be greater than 0");
		}
		else if (request.getParameter(ImageParser.EVENTID) == null)
		{
			logger_.log(Level.SEVERE, "Unable to get wreckid from the request");
		}
		else
		{
			isValid = true;
		}
		return isValid;
	}

	@Override
	public Notification parse(HttpServletRequest req) {
		if (req.getParameter("type").equalsIgnoreCase("eventimagepost")) {
			//get data from request
			
			
			long time, eventId;
			time = System.currentTimeMillis();
			//try{
				eventId = Long.parseLong(req.getParameter(EVENTID));
			//}catch(NumberFormatException e){
				//eventId = Integer.parseInt(req.getParameter(EVENTID));
			//}
			String response = req.getParameter("resp");
			String callback = req.getParameter("callback");
			byte[] imageData = new byte[req.getContentLength() + 1];
			try {
				ServletInputStream sis = req.getInputStream();
				int read = 0;
				int readSoFar = 0;
				while ((read = sis.read (imageData, readSoFar, imageData.length - readSoFar)) != -1)
				{
					readSoFar += read;
					//logger_.log(Level.SEVERE, "Read " + String.valueOf(read) + " bytes this time. So Far " + String.valueOf(readSoFar));
				}
			} catch (IOException excp) {
				logger_.log(Level.SEVERE, "Got IOException:" + excp.getMessage());
			}
			ImageNotification in = new ImageNotification();
			in.setEventId(eventId);
			in.setTime(time);
			in.setBytes(imageData);
			in.setResponseType(response);
			in.setCallback(callback);
			return in;
		} else {
			return null;
		}
	}
 
	
}
