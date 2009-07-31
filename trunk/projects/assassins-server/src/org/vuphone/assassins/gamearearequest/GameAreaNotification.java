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
package org.vuphone.assassins.gamearearequest;

import org.vuphone.assassins.notification.Notification;

/**
 * This class represents a container for information related to a game area
 * notification.
 * 
 * @author Chris Thompson
 * 
 */
public class GameAreaNotification extends Notification {

	private String response_;

	public GameAreaNotification() {
		super("gameAreaGet");

	}

	/**
	 * Returns the string that this notification would like to be returned in
	 * the Http reply
	 * 
	 * @see org.vuphone.wwatch.notification#getResponseString()
	 */
	@Override
	public String getResponseString() {
		return response_;
	}

	public void setResponseString(String response) {
		response_ = response;
	}

}
