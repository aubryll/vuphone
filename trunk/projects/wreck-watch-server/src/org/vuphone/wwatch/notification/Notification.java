/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.notification;

/**
 * This class is a data structure that
 * represents a generic notification of
 * an event, such as an accident.
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
	 * This method returns the type of notification.
	 * The notification type is used to determine which
	 * NotificationHandler implementation should be used
	 * to handle the notification.
	 * @return
	 */
	public String getType() {
		return type_;
	}

	public void setType(String type) {
		type_ = type;
	}

}
