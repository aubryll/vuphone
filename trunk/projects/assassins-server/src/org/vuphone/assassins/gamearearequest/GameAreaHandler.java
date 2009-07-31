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
package org.vuphone.assassins.gamearearequest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vuphone.assassins.notification.InvalidFormatException;
import org.vuphone.assassins.notification.Notification;
import org.vuphone.assassins.notification.NotificationHandler;

public class GameAreaHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
			.getLogger(GameAreaHandler.class.getName());
	
	// XML will instantiate this
	private GameAreaParser parser_;

	/**
	 * This method receives a notification, tries to convert the
	 * notification into a GameAreaNotification, and enters the land
	 * mine in the database.
	 * 
	 * @see org.vuphone.assassins.notification.NotificationHandler#handle(Notification)
	 */
	public Notification handle(Notification n) {

		// Parse the Notification and extract the GameAreaNotification
		GameAreaNotification report;

		try {
			report = parser_.getGameArea(n.getRequest());

		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
			logger_.log(Level.SEVERE, "Unable to parse the Notification:");
			logger_.log(Level.SEVERE, " Notification was: ");
			logger_.log(Level.SEVERE, " " + n.toString());
			logger_.log(Level.SEVERE, 
					" Unable to continue without GameAreaNotification," +
					" stopping");
			return n;
		}
		
		String resp = "Latitude="+ServerGameArea.getInstance().getLatitude()+
				"&Longitude="+ServerGameArea.getInstance().getLongitude()+
				"&Radius="+ServerGameArea.getInstance().getRadius();
		report.setResponseString(resp);

		return report;
	}

	public GameAreaParser getParser() {
		return parser_;
	}

	public void setParser(GameAreaParser parser) {
		parser_ = parser;
	}


}
