package org.vuphone.wwatch.notification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class NotificationServlet extends HttpServlet {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = 1895167101514191256L;

	private static final Logger logger_ = Logger
			.getLogger(NotificationServlet.class.getName());

	private Map<String, NotificationHandler> handlers_;
	private NotificationParser parser_;
	private DataSource ds_;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Notification note = parser_.parse(req, resp);

		if (note == null) {
			logger_.log(Level.WARNING,
					"The parser returned a null notification");
			return;
		}

		NotificationHandler handler = handlers_.get(note.getType());
		if (handler == null) {
			logger_.log(Level.WARNING,
					"The handler returned was null. Check XML validity");
			return;
		}

		Notification rnote = null;
		try {
			rnote = handler.handle(note);
		} catch (HandlerFailedException e1) {
			e1.printStackTrace();
		}
		if (rnote == null) {
			logger_.log(Level.WARNING,
					"The handler.handle function returned a null response");

			return;
		}

		try {
			logger_.log(Level.SEVERE, rnote.getResponseString());
			resp.getWriter().write(rnote.getResponseString());
		}
		catch (IllegalStateException e) {
			if (e.getMessage() == "STREAM") {
				logger_.log(Level.INFO, "NotificationServlet: resp.getWriter "+
						"threw IllegalStateException, as expected when " +
						"writing stream data.");
				logger_.log(Level.INFO, "The response will be returned in " +
						"the output stream instead of in the textwriter.");
			}
			else {
				throw e;
			}
		}

		try {
			ds_.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Map<String, NotificationHandler> getHandlers() {
		return handlers_;
	}

	public void setHandlers(Map<String, NotificationHandler> handlers) {
		handlers_ = handlers;
	}

	public NotificationParser getParser() {
		return parser_;
	}

	public void setParser(NotificationParser parser) {
		parser_ = parser;
	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

}
