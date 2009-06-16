package org.vuphone.wwatch.notification;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serializer.dom3.LSSerializerImpl;
import org.vuphone.wwatch.inforeq.InfoHandledNotification;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSSerializer;

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



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		Notification note = parser_.parse(req);

		if (note != null) {
			NotificationHandler handler = handlers_.get(note.getType());
			if (handler != null) {
				Notification rnote = handler.handle(note);
				if (rnote == null){
					System.out.println("Rnote was null, something went wrong");
					return;
				}
				if (rnote.getType().equalsIgnoreCase("infohandled")){
	
					InfoHandledNotification info = (InfoHandledNotification)rnote;
					//There's probably a better way to do this. Jules,
					//any fancy XML ideas?

					//Build the xml response
					Document d = null;
					try {
						d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					} catch (ParserConfigurationException e) {

						logger_
							.log(
								Level.SEVERE,
								"Parser configuration exception creating document for xml response",
								e);
					}
					Node rootRt = d.createElement("Routes");

					for (Route r:info.getAccidents()){
						Node route = d.createElement("Route");
						Node rootPt = d.createElement("Points");

						for (Waypoint w:r.getRoute()){
							Node pointR = d.createElement("Point");
							Node lat = d.createElement("Latitude");

							lat.appendChild(d.createTextNode(Double.toString(w.getLatitude())));

							pointR.appendChild(lat);

							Node lon = d.createElement("Longitude");
							lon.appendChild(d.createTextNode(Double.toString(w.getLongitude())));
								
							pointR.appendChild(lon);
								
							Node time = d.createElement("Time");
							time.appendChild(d.createTextNode(Long.toString(w.getTime())));

							pointR.appendChild(time);

							rootPt.appendChild(pointR);
						}
						route.appendChild(rootPt);
						rootRt.appendChild(route);

					}
					d.appendChild(rootRt);
					LSSerializer ls = new LSSerializerImpl();
					String xml = ls.writeToString(d);

					resp.getWriter().write(xml);						

				}else{
					resp.getWriter().write(note.toString());
				}

			} else {
				//This will just be temporary to make it do something
				resp.getWriter().write(note.toString());
			}
		}
		else {
			// to do...
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

}
