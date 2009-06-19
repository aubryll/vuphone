package org.vuphone.wwatch.inforeq;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.notification.InvalidFormatException;

/**
 * This class is used to convert generic notification objects into
 * InfoNotification objects.
 * 
 * @author hamilton
 * 
 */
public class InfoParser {
	private Logger log_ = Logger.getLogger(InfoParser.class.getName());

	public InfoNotification getInfo(HttpServletRequest request)
			throws InvalidFormatException {
		
		InfoNotification info = new InfoNotification();

		int latE6;
		double lat;

		int lonE6;
		double lon;
		try {

			info.setTime(Long.parseLong(request.getParameter("maxtime")));
			
			latE6 = Integer.parseInt(request.getParameter("lattl"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request.getParameter("lontl"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setTopLeftCorner(lat, lon);

			latE6 = Integer.parseInt(request.getParameter("lattr"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request.getParameter("lontr"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setTopRightCorner(lat, lon);

			latE6 = Integer.parseInt(request.getParameter("latbl"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request.getParameter("lonbl"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setBottomLeftCorner(lat, lon);

			latE6 = Integer.parseInt(request.getParameter("latbr"));
			lat = (double) latE6;
			lat = lat / 1E6;
			lonE6 = Integer.parseInt(request.getParameter("lonbr"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			info.setBottomRightCorner(lat, lon);

			info.setRequest(request);
		} catch (Exception e) {
			// If we get here, likely the parameters were wrong or not present
			
			e.printStackTrace();
			throw new InvalidFormatException();
		}

		return info;
	}
}
