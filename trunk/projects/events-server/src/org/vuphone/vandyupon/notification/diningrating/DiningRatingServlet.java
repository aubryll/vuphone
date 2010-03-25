 /**************************************************************************
 * Copyright 2009 Chris Thompson Modified 2010 by Hilmi Mustafah  		   *
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
package org.vuphone.vandyupon.notification.diningrating;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.ServerUtils;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
//import org.vuphone.vandyupon.notification.NotificationResponseHandler;



public class DiningRatingServlet extends HttpServlet {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = 1895167101514191256L;

	private static final Logger logger_ = Logger.getLogger(DiningRatingServlet.class.getName());
	/*
	private Map<String, NotificationHandler> handlers_;
	private Map<String, NotificationParser> parsers_;
	private Map<String, NotificationResponseHandler> responders_;
	*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		logger_.log(Level.FINER, "Query was " + req.getQueryString());
		
		/*
		String typeParam = req.getParameter("type");
		if (typeParam == null) {
			logger_.log(Level.WARNING,
			"There was no type parameter. Did the URL forget a '?'?");
			return;
		}
			
		Notification note = parsers_.get(typeParam).parse(req);

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

		ResponseNotification rnote = null;
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
			responders_.get(rnote.getResponseNotificationType()).handle(resp, rnote);
		}
		catch (HandlerFailedException e) {
			e.printStackTrace();
		}
		*/
		DiningParser dp = new DiningParser();
		Notification p = dp.parse(req);
		
		//Test to see if the parser is correct
		if(p instanceof DiningRating)
		{
			System.out.print(((DiningRating)p).getLocation() + " ");
			System.out.print(((DiningRating)p).getDeviceID() + " ");
			System.out.print(((DiningRating)p).getRating() + " \n\n");
		}
		else if(p instanceof DiningRatingRequest)
		{
			System.out.print(((DiningRatingRequest)p).getLocation() + " ");
			System.out.print(((DiningRatingRequest)p).getDeviceID() + " \n\n");
		}
		else
		{
			System.out.print("Error\n");
			System.exit(0);
		}
		
		//Test Handler, and ResponseHandler, and everything else
		NotificationHandler dh = (DiningHandler)ServerUtils.get().getFactory().getBean("DiningHandler");
		
		ResponseNotification nr = null;
		
		try {
			nr = dh.handle(p);//p is declared before parser test.
		} catch (HandlerFailedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			((DiningRatingResponseHandler) ServerUtils.get().getFactory().getBean("DiningRatingResponder")).handle(resp, nr);
	
		} catch (HandlerFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/*
	
	public Map<String, NotificationHandler> getHandlers() {
		return handlers_;
	}

	public void setHandlers(Map<String, NotificationHandler> handlers) {
		handlers_ = handlers;
	}

	public void setParsers(Map<String, NotificationParser> parsers){
		parsers_ = parsers;
	}

	public Map<String, NotificationParser> getParsers(){
		return parsers_;
	}

	public void setResponders(Map<String, NotificationResponseHandler> resp){
		responders_ = resp;
	}

	public Map<String, NotificationResponseHandler> getResponders(){
		return responders_;
	}
	*/

}
