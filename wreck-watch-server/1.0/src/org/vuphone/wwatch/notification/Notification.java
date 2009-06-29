/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.notification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is a data structure that represents a generic notification of an
 * event, such as an accident.
 * 
 * @author jules
 * 
 */
public class Notification {

	private String type_;
	private HttpServletRequest request_ = null;
	private HttpServletResponse response_;

	public Notification(String type) {
		super();
		type_ = type;
	}

	/**
	 * This method returns the HttpServletRequest that generated this
	 * notification. Individual parsers should understand how to read the
	 * ServletRequest that would generate them. For instance, an
	 * AccidentNotification would come with a request, and the AccidentParser
	 * should know the appropriate fields to search for in the request
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return request_;
	}

	/**
	 * This allows a notification to access the raw response, if it so desires.
	 * This is useful for notifications that would like to reply with raw byte
	 * data. To use this response, extend Notification as typical, and override
	 * getResponseString to return "". Use the HttpServletResponse to send your
	 * response, and "" (aka, nothing) will be added to the end
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return response_;
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

	public void setRequest(HttpServletRequest request) {
		request_ = request;
	}

	public void setResponse(HttpServletResponse response) {
		response_ = response;
	}

	public void setType(String type) {
		type_ = type;
	}

	/**
	 * This method should be Overridden by specific Notification subtypes. They
	 * should print out their responses, which will be sent back to the client
	 */
	public String getResponseString() {
		// TODO - log message if this is ever called
		return "";
	}

}
