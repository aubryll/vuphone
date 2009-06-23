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

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.mapping.MapEvent;
import org.vuphone.wwatch.mapping.MapEventParser;

public class RouteRequestParser extends MapEventParser {
	
	public RouteRequestParser(){
		super("locationrequest");
	}

	@Override
	public MapEvent parse(HttpServletRequest req) {
	
		RouteRequestEvent rre = new RouteRequestEvent();
		rre.setNorthEastLat(Double.parseDouble(req.getParameter("nelat")));
		rre.setNorthEastLon(Double.parseDouble(req.getParameter("nelon")));
		rre.setSouthWestLat(Double.parseDouble(req.getParameter("swlat")));
		rre.setSouthWestLon(Double.parseDouble(req.getParameter("swlon")));
		
		return rre;
	}

}
