 /**************************************************************************
 * Copyright 2009 Jules White                                              *
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
package org.vuphone.vandyupon.notification;


/**
 * This class is a data structure that represents a generic notification of an
 * event, such as an accident.
 * 
 * @author jules
 * 
 */
public class Notification {

	private String type_;

	public Notification(String type) {
		super();
		type_ = type;
	}


	/**
	 * This method returns the type of notification. The notification type is
	 * used to determine which NotificationHandler implementation should be used
	 * to handle the notification. The linking of notification types to
	 * notification handlers is done in org.vuphone server.xml, and can be
	 * changed at run time
	 * 
	 * @return string representing the notification type
	 */
	public String getType() {
		return type_;
	}


	public void setType(String type) {
		type_ = type;
	}

	/**
	 * This method should be Overridden by specific Notification subtypes. They
	 * should print out their responses, which will be sent back to the client
	 */
	public String getResponseString() {
		return "";
	}

}
