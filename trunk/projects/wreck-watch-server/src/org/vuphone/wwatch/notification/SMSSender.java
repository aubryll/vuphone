package org.vuphone.wwatch.notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMSSender {

	// Variables for connection
	private static final String SMTP_HOST_NAME_ = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT_ = 465;
	private static final String SMTP_AUTH_USER_ = "vuphone.1@gmail.com";
	private static final String SMTP_AUTH_PWD_ = "isisvuphone.1";

	// Other data required
	private static Properties props_ = new Properties();
	private static Session mailSession_ = Session.getDefaultInstance(props_);

	// Carrier list
	private static String[] carriers = { "message.alltel.com", "txt.att.net",
			"myboostmobile.com", "messaging.nextel.com", "tmomail.net", "vtext.com", "vmobl.com" };

	// Messaging vars
	public static final String SUBJECT = "WreckWatch, Possible Wreck Near You";
	public static final String BODY = "There may have been a wreck near you, please let us know if you see it!";

	/**
	 * Sends a text message to the given 10 digit number by e-mailing it to all
	 * service provider email addresses.
	 * 
	 * @param tenDigitNumber
	 */
	// Note: We could later change the From address and Reply-To address using
	// message.whatever
	public static void sendText(String tenDigitNumber) {

		// Create the Message
		MimeMessage message = new MimeMessage(mailSession_);
		try {
			for (String recp : carriers)
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(tenDigitNumber + "@" + recp));

			message.setSubject(SUBJECT);
			message.setContent(BODY, "text/plain");
		} catch (MessagingException me) {
			System.out.println("SMSSender: Unable to send SMS, "
					+ "could not build message");
			return;
		}

		// Set all properties
		props_.put("mail.transport.protocol", "smtps");
		props_.put("mail.smtps.host", SMTP_HOST_NAME_);
		props_.put("mail.smtps.auth", "true");
		props_.put("mail.smtps.quitwait", "false");
		props_.put("mail.from", "vuphone.1@gmail.com");

		// Send the message
		try {
			Transport transport = mailSession_.getTransport();
			transport.connect(SMTP_HOST_NAME_, SMTP_HOST_PORT_,
					SMTP_AUTH_USER_, SMTP_AUTH_PWD_);

			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (NoSuchProviderException nsp) {
			System.out
					.println("SMSSender: Could not find the transport, unable to send SMS!");
			return;
		} catch (MessagingException me2) {
			System.out.println("SMSSender: Unable to send SMS: " + me2.getMessage());
			return;
		}

		System.out.println("SMSSender: Sent message to " + tenDigitNumber);

	}


}
