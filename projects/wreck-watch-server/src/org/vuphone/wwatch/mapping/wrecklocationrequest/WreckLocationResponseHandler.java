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
package org.vuphone.wwatch.mapping.wrecklocationrequest;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.mapping.MapResponse;
import org.vuphone.wwatch.mapping.MapResponseHandler;
import org.vuphone.wwatch.notification.HandlerFailedException;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class WreckLocationResponseHandler implements MapResponseHandler {

	
	public void respond(MapResponse mresp, HttpServletResponse resp) throws HandlerFailedException {
		
		WreckLocationResponse wlr;
		if (mresp instanceof WreckLocationResponse)
			wlr = (WreckLocationResponse)mresp;
		else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new Exception("MapResponse/Hadler Mismatch:  WreckLocationResponseHandler cannot handle " + mresp.getClass()));
			throw hfe;
		}
		
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.setMode(XStream.NO_REFERENCES);
		
		xs.alias("routes", ArrayList.class);
		xs.alias("route", Route.class);
		xs.alias("waypoint", Waypoint.class);
		
		try {
			resp.getWriter().write(xs.toXML(wlr.getAccidents()));
		} catch (IOException e) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
		
	}

}
