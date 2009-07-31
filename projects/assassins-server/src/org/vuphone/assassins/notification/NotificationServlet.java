/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.notification;

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
		logger_.log(Level.FINER, "Query was " + req.getQueryString());

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
			logger_.log(Level.FINER, rnote.getResponseString());
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
