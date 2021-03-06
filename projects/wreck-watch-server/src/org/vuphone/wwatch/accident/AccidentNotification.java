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
package org.vuphone.wwatch.accident;

import java.text.DateFormat;
import java.util.Date;

import org.vuphone.wwatch.notification.Notification;

/**
 * This class represents a container for information related to an accident
 * notification.
 * 
 * @author Chris Thompson
 * 
 */
public class AccidentNotification extends Notification {

	private double speed_;
	private double dec_;
	private long time_;

	private double lat_;
	private double lon_;
	private String person_;

	public AccidentNotification() {
		super("accident");

	}

	public void setParty(String person) {
		person_ = person;
	}

	public String getPerson() {
		return person_;
	}

	public void setLatitude(double lat) {
		lat_ = lat;
	}

	public void setLongitude(double lon) {
		lon_ = lon;
	}

	public double getLatitude() {
		return lat_;
	}

	public double getLongitude() {
		return lon_;
	}

	public double getSpeed() {
		return speed_;
	}

	public void setSpeed(double speed) {
		speed_ = speed;
	}

	public double getDeceleration() {
		return dec_;
	}

	public void setDeceleration(double dec) {
		dec_ = dec;
	}

	public long getTime() {
		return time_;
	}

	public void setTime(long time) {
		time_ = time;
	}

	/** Simple Override allowing us to print out nice messages */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb
				.append("Speed: " + speed_ + "\n Deceleration: " + dec_
						+ "\n Time: ");
		Date d = new Date(time_);
		DateFormat df = DateFormat.getDateTimeInstance();
		sb.append(df.format(d));

		return sb.toString();

	}

	/**
	 * Returns the string that this notification would like to be returned in
	 * the Http reply
	 * 
	 * @see org.vuphone.wwatch.notification#getResponseString()
	 */
	@Override
	public String getResponseString() {
		return "";
	}

}
