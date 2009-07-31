/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.landmineadd;

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
public class LandMineAddParser {
	private Logger log_ = Logger.getLogger(LandMineAddParser.class.getName());

	public LandMineAddNotification getLandMine(HttpServletRequest request)
			throws InvalidFormatException {

		// Create an AccidentNotification
		LandMineAddNotification lmn = null;
		try {
			log_.log(Level.FINE, "Processing land mine notification");
			lmn = new LandMineAddNotification();
			lmn.setLatitude(Double.parseDouble(request.getParameter("lat")));
			log_.log(Level.FINER, "Latitude: " + lmn.getLatitude());
			lmn.setLongitude(Double.parseDouble(request.getParameter("lon")));
			log_.log(Level.FINER, "Longitude: " + lmn.getLongitude());
			lmn.setRadius(Double.parseDouble(request.getParameter("radius")));
			log_.log(Level.FINER, "Radius: " + lmn.getRadius());
			lmn.setRequest(request);
			log_.log(Level.FINEST, "Request: " + request.toString());
		} catch (Exception e) {
			e.printStackTrace();

			// If one of the parameters is invalid, or is not present
			throw new InvalidFormatException();
		}

		return lmn;

	}
}
