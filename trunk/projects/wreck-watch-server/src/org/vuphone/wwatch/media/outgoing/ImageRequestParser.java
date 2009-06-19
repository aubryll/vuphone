package org.vuphone.wwatch.media.outgoing;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.contacts.ContactParser;
import org.vuphone.wwatch.notification.InvalidFormatException;

public class ImageRequestParser {
	
	private Logger log_ = Logger.getLogger(ContactParser.class.getName());
	
	public ImageRequestNotification getImage(HttpServletRequest request, 
			HttpServletResponse response) throws InvalidFormatException {
		
		ImageRequestNotification irn = null;
		try {
			
			irn = new ImageRequestNotification();
			irn.setRequest(request);
			irn.setResponse(response);
			
			int latE6;
			double lat;

			int lonE6;
			double lon;

			latE6 = Integer.parseInt(request.getParameter("latitude"));
			lat = (double) latE6;
			lat = lat / 1E6;
			irn.setLat(lat);

			lonE6 = Integer.parseInt(request.getParameter("longitude"));
			lon = (double) lonE6;
			lon = lon / 1E6;
			irn.setLon(lon);

			} catch (Exception e) {
				e.printStackTrace();
				log_.log(Level.WARNING, "Exception when parsing ImageRequest request");
				throw new InvalidFormatException();
			}

		return irn;
	}

}
