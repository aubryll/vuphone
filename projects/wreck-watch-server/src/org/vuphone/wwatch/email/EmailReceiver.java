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
package org.vuphone.wwatch.email;

import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3SSLStore;

/**
 * This class is responsible for checking POP3 e-mail accounts.
 * @author Chris Thompson
 *
 */

public class EmailReceiver {

	private static final String HOST = "pop.gmail.com";
	private static final int POP_PORT = 995;

	private Store store_;
	private String username_;
	private String pw_;
	/**
	 * Default constructor 
	 */
	public EmailReceiver(String username, String pw){
		username_ = username;
		pw_ = pw;
	}
	

	/**
	 * This method is used to retrieve mail from a POP server.  
	 * @param folder - Folder to check
	 * @return Message[] - An array of javax.mail.Message objects.  
	 * Array is null if no mail was downloaded.
	 * @throws MessagingException 
	 */
	public Message[] checkMail(String folderName) throws MessagingException{
		
		connect();
		Folder folder = store_.getDefaultFolder();
		folder = folder.getFolder(folderName);
        
		try {            
            folder.open(Folder.READ_WRITE);            
        } catch (MessagingException ex) {            
            folder.open(Folder.READ_ONLY);
        }
        
        Message[] msgs = folder.getMessages();	
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);        
        folder.fetch(msgs, fp);
        
        folder.close(false);
        store_.close();
        
		return msgs;
	}



	/**
	 * Private helper method for connecting to an SSL-enabled pop server
	 * @throws MessagingException
	 */
	private void connect() throws MessagingException {
		//TODO - Support SSL as an option

		Properties pop3Props = new Properties();

		pop3Props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		pop3Props.put("mail.debug", "true");
		pop3Props.setProperty("mail.pop3.port", Integer.toString(POP_PORT));
		pop3Props.setProperty("mail.pop3.socketFactory.port", Integer.toString(POP_PORT));

		URLName url = new URLName("pop3", HOST, POP_PORT, "",
				username_, pw_);

		store_ = new POP3SSLStore(Session.getInstance(pop3Props, null), url);
		store_.connect();

	}

}
