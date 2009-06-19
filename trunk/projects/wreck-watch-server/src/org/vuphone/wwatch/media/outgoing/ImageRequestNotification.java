 /**************************************************************************
 * Copyright 2009 Scott Campbell                                           *
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
package org.vuphone.wwatch.media.outgoing;

import org.vuphone.wwatch.notification.Notification;

public class ImageRequestNotification extends Notification{

	private double lat_ = 0.0;
	
	private double lon_ = 0.0;
	
	public ImageRequestNotification() {
		super("imageRequest");
	}

	public double getLat() {
		return lat_;
	}

	public void setLat(double lat) {
		lat_ = lat;
	}

	public double getLon() {
		return lon_;
	}

	public void setLon(double lon) {
		lon_ = lon;
	}
	
	public String getResponseString() {
		return "";
	}

}
