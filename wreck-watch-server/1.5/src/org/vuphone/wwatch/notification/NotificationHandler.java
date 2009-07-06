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
 * The WreckWatch server does not directly process
 * any notifications that it receives. The server
 * delegates notifications to a series of NotificationHandler
 * instances that perform the actual notification
 * processing logic. This is the base interface for
 * a unit of notification processing logic.
 * 
 * @author jules
 *
 */
public interface NotificationHandler {
	
	/**
	 * This method handles a received notification
	 * and optionally produces another notification as
	 * a response.
	 * 
	 * @param n
	 * @return
	 * @throws HandlerFailedException 
	 */
	public Notification handle(Notification n) throws HandlerFailedException;
	
}
