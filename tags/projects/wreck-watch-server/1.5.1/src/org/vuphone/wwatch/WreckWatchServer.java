/*******************************************************************************
 * Copyright 2009 Jules White
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

package org.vuphone.wwatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.http.HttpServlet;

import org.cometd.Bayeux;
import org.mortbay.cometd.continuation.ContinuationCometdServlet;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.vuphone.wwatch.sql.SqlConstructor;

/**
 * This class is the entry point for the WreckWatch server. The class spawns an
 * HTTP server and registers the core WreckWatch servlets.
 * 
 * @author jules
 * 
 */
public class WreckWatchServer {

	private static final String NOTIFICATION_SERVLET = "notificationServlet";
	private static final String MAP_SERVLET = "mapServlet";
	private static final String JSP_SERVLET = "jspServlet";
	private static final String SQL_CONSTRUCTOR = "sqlConstructor";
	public static final String WRECK_WATCH_PATH = "/wreckwatch";
	public static final String WRECK_WATCH_EVENT_CHANNEL = "/events";
	public static final String WRECK_WATCH_NOTIFICATIONS_CHANNEL = "/notifications";
	public static final String WRECK_WATCH_BROWSER_AJAX_CHANNEL = "/map";
	public static final String WRECK_WATCH_BROWSER_CHANNEL = "/wrecks";
	

	private static final Logger logger_ = Logger
			.getLogger(WreckWatchServer.class.getName());

	private boolean running_ = false;

	private static Server server_;

	/**
	 * Stops the WreckWatch server.
	 */
	public void stop() {
		try {
			server_.stop();
		} catch (Exception e) {
		}
		running_ = false;
	}

	/**
	 * Entry Point
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				// Need to modify this to print the correct usage...
				logger_.log(Level.SEVERE, "Probably an invalid port number:"
						+ args[0], e);
				return;
			}
		}

		(new WreckWatchServer()).start(port);
	}

	/**
	 * Starts the WreckWatch server on the specified port.
	 * 
	 * @param port
	 */
	public void start(int port) {
		if (running_)
			return;

		running_ = true;

		server_ = new Server(port);

		Context context = new Context(server_, "/", Context.SESSIONS);
		ContinuationCometdServlet cometd = new ContinuationCometdServlet();
		context.addServlet(new ServletHolder(cometd), WRECK_WATCH_PATH
				+ WRECK_WATCH_EVENT_CHANNEL + "/*");
		
		context.addEventListener(new ServletContextAttributeListener() {

			public void attributeReplaced(ServletContextAttributeEvent arg0) {
			}

			public void attributeRemoved(ServletContextAttributeEvent arg0) {
			}

			public void attributeAdded(ServletContextAttributeEvent event) {
				if (Bayeux.DOJOX_COMETD_BAYEUX.equals(event.getName()))
					EventChannelManager.launch((Bayeux) event.getValue());
			}
		});
		
		HttpServlet notificationServlet = (HttpServlet)ServerUtils.get().getFactory().getBean(NOTIFICATION_SERVLET);
		context.addServlet(new ServletHolder(notificationServlet), WRECK_WATCH_PATH
				+ WRECK_WATCH_NOTIFICATIONS_CHANNEL + "/*");
		
		HttpServlet mapServlet = (HttpServlet)ServerUtils.get().getFactory().getBean(MAP_SERVLET);
		context.addServlet(new ServletHolder(mapServlet), WRECK_WATCH_PATH + WRECK_WATCH_BROWSER_AJAX_CHANNEL + "/*");
		
		HttpServlet jspServlet = (HttpServlet)ServerUtils.get().getFactory().getBean(JSP_SERVLET);
		context.addServlet(new ServletHolder(jspServlet), WRECK_WATCH_PATH + WRECK_WATCH_BROWSER_CHANNEL + "/*.jsp");
		
		
		context.setResourceBase("html/");

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setBaseResource(context.getBaseResource());
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		server_.setHandler(handlers);
		

		try {
			server_.start();
			SqlConstructor sql = (SqlConstructor)ServerUtils.get().getFactory().getBean(SQL_CONSTRUCTOR);
			sql.prepareDatabase();
		} catch (Exception e) {
			logger_
					.log(
							Level.SEVERE,
							"Something went horribly wrong...and we have no idea why....",
							e);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true){
			try {
				if (br.readLine().equalsIgnoreCase("stop")){
					stop();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
