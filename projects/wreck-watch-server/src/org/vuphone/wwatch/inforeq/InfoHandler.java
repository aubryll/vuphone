 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.inforeq;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class InfoHandler implements NotificationHandler {

	
	@Override
	public Notification handle(Notification n) {
		InfoNotification info = (InfoNotification)n;
		
		//I hate myself for doing this...
		String sql = "select * from accidents where lat between " + info.getTopLeftCorner().getLatitude() + " and " +
			info.getBottomLeftCorner().getLatitude() + " and lon between " + info.getTopLeftCorner().getLongitude() + 
			" and " + info.getTopRightCorner().getLongitude();
		
		//Number used for now, will fill with number of rows
		
		/*
		 * For testing purposes, we'll hard-code this
		 */
		InfoHandledNotification note = new InfoHandledNotification(2);
		/*
		 * for each row:
		 * 		call note.addWaypoint (lat, lon)
		 */
		
		note.addWaypoint(37.413532, -122.072855);
		note.addWaypoint(37.421975, -122.084054);
		return note;
	}

}
