/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
package org.vuphone.wwatch.accident;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

/**
 * This class is responsible for receiving accident reports
 * and kicking off the accident response process.
 * 
 * @author jules
 *
 */
public class AccidentHandler implements NotificationHandler {

	private static final Logger logger_ = Logger
			.getLogger(AccidentHandler.class.getName());

	private AccidentParser parser_;

	/**
	 * This method receives a notification of an accident,
	 * converts the notification into an AccidentReport,
	 * and then determines the proper way to respond to the
	 * accident.
	 */
	public Notification handle(Notification n) {
		Notification response = null;
		try {
			AccidentReport report = parser_.getAccident(n);
			
			//do something with the report
		} catch (AccidentFormatException e) {
			logger_.log(Level.SEVERE,
					"Error extracting accident report from notification", e);
		}
		return response;
	}

	public AccidentParser getParser() {
		return parser_;
	}

	public void setParser(AccidentParser parser) {
		parser_ = parser;
	}

}
