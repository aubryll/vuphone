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
package org.vuphone.wwatch.mapping.routerequest;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.mapping.MapResponse;
import org.vuphone.wwatch.mapping.MapResponseHandler;
import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.routing.Waypoint;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class RouteResponseHandler implements MapResponseHandler {

	
	public void respond(MapResponse mresp, HttpServletResponse resp, String callback) throws HandlerFailedException {
		
		RouteResponse rr;
		if (mresp instanceof RouteResponse)
			rr = (RouteResponse)mresp;
		else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new Exception("MapResponse/Hadler Mismatch:  WreckLocationResponseHandler cannot handle " + mresp.getClass()));
			throw hfe;
		}
		
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.setMode(XStream.NO_REFERENCES);
		
		
		
		xs.alias("routes", ArrayList.class);
		xs.alias("route", EncodedRoute.class);
		xs.alias("waypoint", Waypoint.class);
		String response;
		if (callback == null){
			response = xs.toXML(rr.getAccidents());
		}else{
			response = callback + "( " + xs.toXML(rr.getAccidents()) + " )";
		}
		try {
			resp.getWriter().write(response);
		} catch (IOException e) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
		
	}

}
