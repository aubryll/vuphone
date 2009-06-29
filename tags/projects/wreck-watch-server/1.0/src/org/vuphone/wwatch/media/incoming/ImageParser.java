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
package org.vuphone.wwatch.media.incoming;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.InvalidFormatException;

/**
 * This class is used to convert generic notification objects into
 * AccidentReport objects.
 * 
 * @author jules
 * 
 */
public class ImageParser {
	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private static final String TIME = "time";
	private static final String WRECKID = "wreckid";
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
		else if (request.getParameter(ImageParser.WRECKID) == null)
		{
			logger_.log(Level.SEVERE, "Unable to get wreckid from the request");
		}
		else
		{
			isValid = true;
		}
		return isValid;
	}
	
	public ImageNotification getImageNotification(Notification notification)
		throws InvalidFormatException {
		if (notification.getType().equalsIgnoreCase("image")) {
			//get data from request
			HttpServletRequest request = notification.getRequest();
			if (!isRequestValid(request))
			{
				logger_.log(Level.SEVERE, "Request didn't have needed parameters");
				throw new InvalidFormatException();
			}
			
			long time, wreckId;
			time = Long.parseLong(request.getParameter(ImageParser.TIME));
			wreckId = Long.parseLong(request.getParameter(ImageParser.WRECKID));
			byte[] imageData = new byte[request.getContentLength() + 1];
			try {
				ServletInputStream sis = request.getInputStream();
				int read = 0;
				int readSoFar = 0;
				while ((read = sis.read (imageData, readSoFar, imageData.length - readSoFar)) != -1)
				{
					readSoFar += read;
					logger_.log(Level.SEVERE, "Read " + String.valueOf(read) + " bytes this time. So Far " + String.valueOf(readSoFar));
				}
			} catch (IOException excp) {
				logger_.log(Level.SEVERE, "Got IOException:" + excp.getMessage());
				throw new InvalidFormatException();
			}
			ImageNotification in = new ImageNotification();
			in.setWreckId(wreckId);
			in.setTime(time);
			in.setBytes(imageData);
			return in;
		} else {
			throw new InvalidFormatException();
		}
	}
 
	
}
