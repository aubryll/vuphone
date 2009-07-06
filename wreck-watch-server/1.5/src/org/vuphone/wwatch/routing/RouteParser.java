package org.vuphone.wwatch.routing;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.notification.InvalidFormatException;

public class RouteParser {
	private Logger log_ = Logger.getLogger(RouteParser.class.getName());

	public RouteNotification getRoute(HttpServletRequest request)
			throws InvalidFormatException {

		RouteNotification rn = new RouteNotification();

		try {
			rn.setRequest(request);
			rn.setPerson(request.getParameter("id"));

			String[] lats = request.getParameterValues("lat");
			String[] lons = request.getParameterValues("lon");
			String[] times = request.getParameterValues("timert");

			if (lats != null) {

				for (int i = 0; i < lats.length; ++i) {
					if (lats[i] != null && lons[i] != null && times[i] != null) {
						rn
								.addWaypoint(Double.parseDouble(lats[i]),
										Double.parseDouble(lons[i]), Long
												.parseLong(times[i]));
					}
				}
			} else {
				rn.addWaypoint(0, 0, System.currentTimeMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log_.log(Level.WARNING, "Exception when parsing route request");
			throw new InvalidFormatException();
		}

		return rn;
	}
}
