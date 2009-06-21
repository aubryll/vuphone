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
package org.vuphone.wwatch.mapping;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.notification.HandlerFailedException;

public class MapServlet extends HttpServlet {

	private Map<String, MapEventHandler> eventHandlers_;
	private Map<String, MapResponseHandler> responseHandlers_;
	private Map<String, MapEventParser> parsers_;
	/**
	 * 
	 */
	private static final long serialVersionUID = -278111903083218074L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Receiving request..."+ req.getQueryString());
		MapEvent e = parsers_.get(req.getParameter("type")).parse(req);
		MapResponse r = null;
		
		try {
			r = eventHandlers_.get(e.getType()).handle(e);
			responseHandlers_.get(r.getType()).respond(r, resp);
		} catch (HandlerFailedException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public Map<String, MapEventHandler> getEventHandlers() {
		return eventHandlers_;
	}

	public void setEventHandlers(Map<String, MapEventHandler> handlers) {
		eventHandlers_ = handlers;
	}
	public Map<String, MapResponseHandler> getResponseHandlers() {
		return responseHandlers_;
	}

	public void setResponseHandlers(Map<String, MapResponseHandler> handlers) {
		responseHandlers_ = handlers;
	}
	
	public Map<String, MapEventParser> getParsers(){
		return parsers_;
	}
	public void setParsers(Map<String, MapEventParser> parsers){
		parsers_ = parsers;
	}


}
