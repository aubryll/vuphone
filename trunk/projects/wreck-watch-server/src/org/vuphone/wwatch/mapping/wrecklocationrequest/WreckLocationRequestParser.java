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

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.mapping.MapEvent;
import org.vuphone.wwatch.mapping.MapEventParser;

public class WreckLocationRequestParser extends MapEventParser {
	
	public WreckLocationRequestParser(){
		super("locationrequest");
	}

	@Override
	public MapEvent parse(HttpServletRequest req) {
	
		WreckLocationRequestEvent wlre = new WreckLocationRequestEvent();
		wlre.setNorthEastLat(Double.parseDouble(req.getParameter("nelat")));
		wlre.setNorthEastLon(Double.parseDouble(req.getParameter("nelon")));
		wlre.setSouthWestLat(Double.parseDouble(req.getParameter("swlat")));
		wlre.setSouthWestLon(Double.parseDouble(req.getParameter("swlon")));
		
		return wlre;
	}

}
