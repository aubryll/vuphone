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
package org.vuphone.wwatch.accident;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.notification.InvalidFormatException;

/**
 * This class is used to convert generic notification objects into
 * AccidentReport objects.
 * 
 * @author jules
 * 
 */
public class AccidentParser {
	private Logger log_ = Logger.getLogger(AccidentParser.class.getName());

	public AccidentNotification getAccident(HttpServletRequest request)
			throws InvalidFormatException {

		// Create an AccidentNotification
		AccidentNotification an = null;
		try {
			log_.log(Level.FINE, "Processing accident notification");
			an = new AccidentNotification();
			an.setSpeed(Double.parseDouble(request.getParameter("speed")));
			log_.log(Level.FINER, "Speed: " + an.getSpeed());
			an.setDeceleration(Double.parseDouble(request.getParameter("dec")));
			log_.log(Level.FINER, "Acceleration: " + an.getDeceleration());
			an.setTime(Long.parseLong(request.getParameter("time")));
			log_.log(Level.FINER, "Time: " + an.getTime());
			an.setParty(request.getParameter("user"));
			log_.log(Level.FINER, "User: " + an.getPerson());
			an.setLatitude(Double.parseDouble(request.getParameter("lat")));
			log_.log(Level.FINER, "Latitude: " + an.getLatitude());
			an.setLongitude(Double.parseDouble(request.getParameter("lon")));
			log_.log(Level.FINER, "Longitude: " + an.getLongitude());
			an.setRequest(request);
			log_.log(Level.FINEST, "Reuest: " + request.toString());
		} catch (Exception e) {
			e.printStackTrace();

			// If one of the parameters is invalid, or is not present
			throw new InvalidFormatException();
		}

		return an;

	}
}
