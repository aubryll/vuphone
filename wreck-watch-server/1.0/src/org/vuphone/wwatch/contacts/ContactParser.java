package org.vuphone.wwatch.contacts;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.notification.InvalidFormatException;

public class ContactParser {
	private Logger log_ = Logger.getLogger(ContactParser.class.getName());

	public ContactNotification getContact(HttpServletRequest request)
			throws InvalidFormatException {

		ContactNotification cn = null;
		try {
			String id = request.getParameter("id");
			cn = new ContactNotification();
			cn.setRequest(request);
			cn.setAndroidID(id);
			cn.setNumbers(request.getParameterValues("number"));
		} catch (Exception e) {
			e.printStackTrace();
			log_.log(Level.WARNING, "Exception when parsing contact request");
			throw new InvalidFormatException();
		}

		return cn;

	}
}
