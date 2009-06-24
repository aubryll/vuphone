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
		
		double tlLon, tlLat, brLon, brLat;
		try {

			tlLat = Double.parseDouble(request.getParameter("lattl")) / 1E6;
			tlLon = Double.parseDouble(request.getParameter("lontl")) / 1E6;
			
			brLat = Double.parseDouble(request.getParameter("latbr")) / 1E6;
			brLon = Double.parseDouble(request.getParameter("lonbr")) / 1E6;
			
			info.setBottomRightCorner(brLat, brLon);
			info.setTopLeftCorner(tlLat, tlLon);
			
			info.setRequest(request);
		} catch (Exception e) {
			// If we get here, likely the parameters were wrong or not present
			
			e.printStackTrace();
			throw new InvalidFormatException();
		}

		return info;
	}
}
