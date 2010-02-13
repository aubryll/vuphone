package org.vuphone.vandyupon.notification.diningrating;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationParser;

public class DiningPostParser implements NotificationParser{

	@Override
	public Notification parse(HttpServletRequest req) {
		//Use UTF-8 Encoding <-- coppied from EventPostParser
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//If the type is not dining as declared in constructor of DiningPost don't do anything
		if (!req.getParameter("type").equalsIgnoreCase("diningrating")) {
			return null;
		}
		
		//req.getParameter(); <--- this commands parse the stringline from device(i think? according to EventPostParser)
		
		//Check If the user is rating or requesting a rating.
		if(req.getParameter("postType").equalsIgnoreCase("rating"))
		{
			return new DiningPost(
					Integer.parseInt(req.getParameter(req.getParameter("loc"))),
					Integer.parseInt(req.getParameter("score"))
					);//Return a DiningPost object
		}
		else if(req.getParameter("postType").equalsIgnoreCase("request"))
		{
			return new DiningPost(
					Integer.parseInt(req.getParameter(req.getParameter("loc")))
					);//Return a DiningPost object
		}
		else
		{
			return null;
		}
	}
/*
 This Parser doesn't deal with deviceID yet. (wait until Mike update DiningPost structure)
 */
}
