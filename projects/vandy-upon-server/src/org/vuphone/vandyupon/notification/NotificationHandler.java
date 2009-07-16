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
	public ResponseNotification handle(Notification n) throws HandlerFailedException;
	
}
