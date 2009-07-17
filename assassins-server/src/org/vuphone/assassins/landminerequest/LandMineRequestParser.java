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
package org.vuphone.assassins.landminerequest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.assassins.notification.InvalidFormatException;

/**
 * This class is used to convert generic notification objects into
 * LandMineNotification objects.
 * 
 * @author jules
 * 
 */
public class LandMineRequestParser {
	private Logger log_ = Logger.getLogger(LandMineRequestParser.class.getName());

	public LandMineRequestNotification getLandMine(HttpServletRequest request)
			throws InvalidFormatException {

		// Create an AccidentNotification
		LandMineRequestNotification lmn = null;
		try {
			log_.log(Level.FINE, "Processing land mine notification");
			lmn = new LandMineRequestNotification();
			lmn.setRequest(request);
			log_.log(Level.FINEST, "Reuest: " + request.toString());
		} catch (Exception e) {
			e.printStackTrace();

			// If one of the parameters is invalid, or is not present
			throw new InvalidFormatException();
		}

		return lmn;

	}
}
