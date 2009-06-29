package org.vuphone.wwatchBranch;

import java.util.Scanner;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

// See : http://www.igniterealtime.org/community/message/183770#183770
/**
 * It appears there is a bug in this release of smack that makes it tricky to
 * login. there is a patch for the bug posted at the above site, but I will wait
 * for some help to get it up and running. For now, this will work for GTalk
 * and jabber
 */

/**
 * I set up an account on jabber.org with username vuphone pass testing
 */

public class JabberExample {
	private static final String username = "vuphone";
	private static final String username2 = "pcaddict06";

	private static final String pass = "testing";

	public static void main(String[] args) {
		// Create a connection to the jabber.org server on a specific port

		// ConnectionConfiguration config = new ConnectionConfiguration(
		// "talk.google.com", 5222, "gmail.com");
		ConnectionConfiguration config = new ConnectionConfiguration(
				"jabber.org", 5222, "jabber.org");

		XMPPConnection connection = new XMPPConnection(config);

		try {
			connection.connect();
		} catch (XMPPException e) {
			e.printStackTrace();
		}

		// Pause for a small time before trying to login.
		// This prevents random ssl exception from Smack API
		try {
			Thread.sleep(100);
		} catch (InterruptedException e5) {
			e5.printStackTrace();
		}

		System.out.println("login");
		if (connection.isConnected()) {
			System.out.println("Connected!");
			if (connection.isAuthenticated() == false) {
				try {
					connection.login("vuphone", "testing");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("here");

		// Create a new presence. Pass in false to indicate we're available.
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);

		Scanner keys = new Scanner(System.in);

		ChatManager chatmanager = connection.getChatManager();
		Chat newChat = chatmanager.createChat(username2 + "@jabber.org",
				new MessageListener() {
					public void processMessage(Chat chat, Message message) {
						System.out.println("Received message: "
								+ message.getBody());
					}
				});

		boolean cont = true;
		String message;
		while (cont) {
			message = keys.nextLine();
			if (message.equalsIgnoreCase("quit"))
				cont = false;

			try {
				newChat.sendMessage(message);
			} catch (XMPPException e) {
				System.out.println("Error Delivering block");
			}
		}

		System.out.println("all done");
	}

}
